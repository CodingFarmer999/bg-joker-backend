package com.bg.joker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.bg.joker.enums.GameType;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventRequest {
    private String eventType;
    private GameType gameType;
    private String title;
    private String description;
    private OffsetDateTime eventTime;
    private Integer maxParticipants;
}
