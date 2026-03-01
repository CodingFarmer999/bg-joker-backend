package com.bg.joker.service;

import com.bg.joker.dto.EventResponse;
import com.bg.joker.entity.Event;
import com.bg.joker.repository.EventParticipantRepository;
import com.bg.joker.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventParticipantRepository eventParticipantRepository;

    @Transactional(readOnly = true)
    public List<EventResponse> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    private EventResponse convertToResponse(Event event) {
        long currentParticipants = eventParticipantRepository.countByEventIdAndJoinStatus(event.getId(), "JOINED");

        return EventResponse.builder()
                .id(event.getId())
                .shopId(event.getShopId())
                .organizer_id(event.getOrganizerId())
                .eventType(event.getEventType())
                .gameType(event.getGameType())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventTime(event.getEventTime())
                .maxParticipants(event.getMaxParticipants())
                .currentParticipants((int) currentParticipants)
                .status(event.getStatus())
                .build();
    }
}
