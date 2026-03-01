package com.bg.joker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEventRequest {
    private Long shopId;
    private Long organizerId;
    private String eventType;
    private String gameType;
    private String title;
    private String description;
    private OffsetDateTime eventTime;
    private Integer maxParticipants;
}
