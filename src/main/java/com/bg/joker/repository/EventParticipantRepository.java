package com.bg.joker.repository;

import com.bg.joker.entity.EventParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {
    List<EventParticipant> findByEventIdAndJoinStatus(Long eventId, String joinStatus);

    Optional<EventParticipant> findByEventIdAndUserIdAndJoinStatus(Long eventId, Long userId, String joinStatus);

    long countByEventIdAndJoinStatus(Long eventId, String joinStatus);
}
