package com.bg.joker.repository;

import com.bg.joker.entity.UserDeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDeviceTokenRepository extends JpaRepository<UserDeviceToken, Long> {

    Optional<UserDeviceToken> findByUserIdAndFcmToken(Long userId, String fcmToken);

    List<UserDeviceToken> findByUserId(Long userId);

    List<UserDeviceToken> findByFcmToken(String fcmToken);

    void deleteByFcmToken(String fcmToken);
}
