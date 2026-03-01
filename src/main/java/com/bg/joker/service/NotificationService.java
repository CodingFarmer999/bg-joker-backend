package com.bg.joker.service;

import com.bg.joker.entity.Notification;
import com.bg.joker.entity.User;
import com.bg.joker.repository.NotificationRepository;
import com.bg.joker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final UserDeviceTokenService userDeviceTokenService;
    private final FcmService fcmService;

    /**
     * Synchronously save notifications to the database for all users,
     * then asynchronously trigger the FCM push so the API returns quickly.
     */
    @Transactional
    public String createAndSendBroadcast(String title, String body) {
        log.info("Starting broadcast process: title='{}'", title);

        // 1. Find all active users (or all users depending on requirements)
        List<User> allUsers = userRepository.findAll();

        // 2. Persist a Notification record for each user synchronously
        List<Notification> notifications = allUsers.stream().map(user -> Notification.builder()
                .userId(user.getId())
                .title(title)
                .body(body)
                .type("BROADCAST")
                .build()).collect(Collectors.toList());

        notificationRepository.saveAll(notifications);
        log.info("Saved {} notification records to the database.", notifications.size());

        // 3. Trigger FCM Push Asynchronously
        sendFcmBroadcastAsync(title, body);

        // 4. Return success immediately
        return "Broadcast notifications saved to DB and FCM push initiated asynchronously.";
    }

    /**
     * Asynchronously perform the actual FCM network request.
     */
    @Async
    public void sendFcmBroadcastAsync(String title, String body) {
        log.info("Async FCM broadcast started running in thread: {}", Thread.currentThread().getName());
        try {
            // Reusing the existing FCM broadcast logic
            userDeviceTokenService.broadcastToAll(title, body, fcmService);
        } catch (Exception e) {
            log.error("Failed to send async FCM broadcast", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            if (notification.getUserId().equals(userId) && !notification.getIsRead()) {
                notification.setIsRead(true);
                notification.setReadAt(java.time.OffsetDateTime.now());
                notificationRepository.save(notification);
            }
        });
    }
}
