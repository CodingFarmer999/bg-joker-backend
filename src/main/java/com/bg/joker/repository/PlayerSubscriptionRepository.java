package com.bg.joker.repository;

import com.bg.joker.entity.PlayerSubscription;
import com.bg.joker.enums.GameType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerSubscriptionRepository extends JpaRepository<PlayerSubscription, Long> {

    // 1. 防呆機制：確保不會重複訂閱
    Optional<PlayerSubscription> findByUserIdAndGameTypeAndShopId(Long userId, GameType gameType, Long shopId);

    // 2. 查詢效能優化：針對店長發送推播時的查詢情境
    List<PlayerSubscription> findByShopIdAndGameTypeAndIsActive(Long shopId, GameType gameType, Boolean isActive);

    // 3. 針對玩家查詢自己訂閱清單
    List<PlayerSubscription> findByUserId(Long userId);
}
