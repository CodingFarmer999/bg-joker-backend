package com.bg.joker.repository;

import com.bg.joker.entity.EventParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {
    List<EventParticipant> findByEventId(Long eventId);

    long countByEventIdAndJoinStatus(Long eventId, String joinStatus);

    @Modifying
    @Transactional
    void deleteByEventId(Long eventId);
}
