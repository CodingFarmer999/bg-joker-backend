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
public class EventResponse {
    private Long id;
    private Long shopId;
    private Long organizer_id;
    private String eventType;
    private String gameType;
    private String title;
    private String description;
    private OffsetDateTime eventTime;
    private Integer maxParticipants;
    private Integer currentParticipants; // 額外計算的目前人數
    private String status;
}
