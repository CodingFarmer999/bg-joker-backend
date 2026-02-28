package com.bg.joker.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FcmService {

    public String sendNotification(String targetToken, String title, String body) {
        log.info("Attempting to send FCM notification to token: {}", targetToken);
        try {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            Message message = Message.builder()
                    .setToken(targetToken)
                    .setNotification(notification)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Successfully sent FCM message: {}", response);
            return "Successfully sent message: " + response;
        } catch (Exception e) {
            log.error("Error sending FCM message to token {}: {}", targetToken, e.getMessage(), e);
            return "Error sending message: " + e.getMessage();
        }
    }

    public String sendMulticastNotification(java.util.List<String> targetTokens, String title, String body) {
        if (targetTokens == null || targetTokens.isEmpty()) {
            return "No tokens provided";
        }

        log.info("Attempting to send FCM multicast notification to {} tokens", targetTokens.size());
        try {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            com.google.firebase.messaging.MulticastMessage message = com.google.firebase.messaging.MulticastMessage
                    .builder()
                    .addAllTokens(targetTokens)
                    .setNotification(notification)
                    .build();

            com.google.firebase.messaging.BatchResponse response = FirebaseMessaging.getInstance()
                    .sendEachForMulticast(message);
            log.info("Successfully sent FCM multicast message. Success: {}, Failure: {}", response.getSuccessCount(),
                    response.getFailureCount());
            return "Successfully sent multicast message. Success: " + response.getSuccessCount() + ", Failure: "
                    + response.getFailureCount();
        } catch (Exception e) {
            log.error("Error sending FCM multicast message: {}", e.getMessage(), e);
            return "Error sending multicast message: " + e.getMessage();
        }
    }
}
