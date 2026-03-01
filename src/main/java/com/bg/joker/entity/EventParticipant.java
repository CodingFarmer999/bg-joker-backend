package com.bg.joker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "event_participants", indexes = {
        @Index(name = "idx_participants_event", columnList = "event_id, join_status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "join_status", nullable = false, length = 20)
    @Builder.Default
    private String joinStatus = "JOINED";

    @CreationTimestamp
    @Column(name = "joined_at", updatable = false)
    private OffsetDateTime joinedAt;
}
