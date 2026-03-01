package com.bg.joker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "events", indexes = {
        @Index(name = "idx_events_shop_time", columnList = "shop_id, event_time, status"),
        @Index(name = "idx_events_game_type", columnList = "game_type, status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @Column(name = "organizer_id", nullable = false)
    private Long organizerId;

    @Column(name = "event_type", nullable = false, length = 20)
    @Builder.Default
    private String eventType = "CASUAL";

    @Column(name = "game_type", nullable = false, length = 50)
    private String gameType;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "event_time", nullable = false)
    private OffsetDateTime eventTime;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "OPEN";

    @Version
    private Long version;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
