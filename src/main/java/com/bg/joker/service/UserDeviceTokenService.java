package com.bg.joker.service;

import com.bg.joker.entity.UserDeviceToken;
import com.bg.joker.repository.UserDeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDeviceTokenService {

    private final UserDeviceTokenRepository tokenRepository;

    @Transactional
    public void upsertUserDeviceToken(Long userId, String fcmToken, String deviceType) {
        log.info("Upserting device token for user: {}", userId);

        // Removed logic that deleted tokens for other users.
        // Multiple users can share the same device and FCM token.

        // 接著處理現在這個使用者的紀錄
        tokenRepository.findByUserIdAndFcmToken(userId, fcmToken).ifPresentOrElse(
                existingToken -> {
                    // 已經存在，只更新活躍時間與裝置類型
                    existingToken.setLastActiveAt(OffsetDateTime.now());
                    if (deviceType != null) {
                        existingToken.setDeviceType(deviceType);
                    }
                    tokenRepository.save(existingToken);
                    log.info("Updated existing token for user: {}", userId);
                },
                () -> {
                    // 如果這個 Token 對這個登入者來說是全新的，就新增一筆
                    UserDeviceToken newToken = UserDeviceToken.builder()
                            .userId(userId)
                            .fcmToken(fcmToken)
                            .deviceType(deviceType != null ? deviceType : "WEB")
                            .build();
                    tokenRepository.save(newToken);
                    log.info("Created new token for user: {}", userId);
                });
    }

    public String broadcastToAll(String title, String body, FcmService fcmService) {
        log.info("Broadcasting message to all devices: title={}", title);

        java.util.List<String> allTokens = tokenRepository.findAll().stream()
                .map(UserDeviceToken::getFcmToken)
                .filter(token -> token != null && !token.trim().isEmpty())
                .distinct()
                .toList();

        if (allTokens.isEmpty()) {
            return "No device tokens found in database.";
        }

        return fcmService.sendMulticastNotification(allTokens, title, body);
    }
}
