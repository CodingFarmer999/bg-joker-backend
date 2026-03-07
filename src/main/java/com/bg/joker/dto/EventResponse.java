package com.bg.joker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import com.bg.joker.enums.GameType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {
    private Long id;
    private Long shopId;
    private Long organizer_id;
    private String eventType;
    private GameType gameType;
    private String title;
    private String description;
    private OffsetDateTime eventTime;
    private Integer maxParticipants;
    private Integer currentParticipants; // 額外計算的目前人數
    private String status;
}
