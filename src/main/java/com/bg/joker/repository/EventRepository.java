package com.bg.joker.repository;

import com.bg.joker.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bg.joker.enums.GameType;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByShopIdAndStatus(Long shopId, String status);

    List<Event> findByGameTypeAndStatus(GameType gameType, String status);
}
