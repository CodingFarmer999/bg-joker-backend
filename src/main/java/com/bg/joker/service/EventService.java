package com.bg.joker.service;

import com.bg.joker.dto.EventResponse;
import com.bg.joker.dto.ParticipantResponse;
import com.bg.joker.entity.Event;
import com.bg.joker.entity.EventParticipant;
import com.bg.joker.entity.User;
import com.bg.joker.repository.EventParticipantRepository;
import com.bg.joker.repository.EventRepository;
import com.bg.joker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final UserRepository userRepository;

    @Cacheable("events")
    @Transactional(readOnly = true)
    public List<EventResponse> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @CacheEvict(value = "events", allEntries = true)
    @Transactional
    public EventResponse createEvent(com.bg.joker.dto.CreateEventRequest request) {
        Event event = Event.builder()
                .shopId(request.getShopId() != null ? request.getShopId() : 1L)
                .organizerId(request.getOrganizerId())
                .eventType(request.getEventType())
                .gameType(request.getGameType())
                .title(request.getTitle())
                .description(request.getDescription())
                .eventTime(request.getEventTime())
                .maxParticipants(request.getMaxParticipants())
                .status("OPEN")
                .build();

        Event savedEvent = eventRepository.save(event);
        return convertToResponse(savedEvent);
    }

    @CacheEvict(value = "events", allEntries = true)
    @Transactional
    public void deleteEvent(Long id) {
        eventParticipantRepository.deleteByEventId(id);
        eventRepository.deleteById(id);
    }

    @CacheEvict(value = "events", allEntries = true)
    @Transactional
    public EventResponse updateEvent(Long id, com.bg.joker.dto.UpdateEventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        long currentParticipants = eventParticipantRepository.countByEventIdAndJoinStatus(id, "JOINED");
        if (request.getMaxParticipants() < currentParticipants) {
            throw new RuntimeException("人數上限不得小於目前已報名人數 (" + currentParticipants + "人)");
        }

        event.setEventType(request.getEventType());
        event.setGameType(request.getGameType());
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setEventTime(request.getEventTime());
        event.setMaxParticipants(request.getMaxParticipants());

        Event updatedEvent = eventRepository.save(event);
        return convertToResponse(updatedEvent);
    }

    @Transactional(readOnly = true)
    public List<ParticipantResponse> getParticipants(Long eventId) {
        List<EventParticipant> participants = eventParticipantRepository.findByEventId(eventId);
        return participants.stream().map(p -> {
            User user = userRepository.findById(p.getUserId())
                    .orElse(null);
            return ParticipantResponse.builder()
                    .id(p.getId())
                    .userId(p.getUserId())
                    .userName(user != null ? user.getDisplayName() : "未知用戶")
                    .joinStatus(p.getJoinStatus())
                    .joinedAt(p.getJoinedAt())
                    .build();
        }).collect(Collectors.toList());
    }

    @CacheEvict(value = "events", allEntries = true)
    @Transactional
    public void updateParticipantStatus(Long participantId, String newStatus) {
        EventParticipant participant = eventParticipantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found"));
        participant.setJoinStatus(newStatus);
        eventParticipantRepository.save(participant);
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
