package com.bg.joker.controller;

import com.bg.joker.dto.EventResponse;
import com.bg.joker.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bg.joker.dto.CreateEventRequest;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResponse>> getEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody CreateEventRequest request) {
        return ResponseEntity.ok(eventService.createEvent(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
