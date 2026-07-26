package com.mkuzhvv.ticketbookingservice.controller;

import com.mkuzhvv.ticketbookingservice.model.dto.EventCreateRequest;
import com.mkuzhvv.ticketbookingservice.model.dto.EventResponse;
import com.mkuzhvv.ticketbookingservice.controller.exception.AccessDeniedException;
import com.mkuzhvv.ticketbookingservice.service.EventService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
            @Valid @RequestBody EventCreateRequest request,
            @RequestHeader(value = "X-User-Role", defaultValue = "USER") String role
            ) {

        log.info("POST /api/events - request to create event, role={}", role);

        if (!"ADMIN".equalsIgnoreCase(role)) {
            log.warn("Trying create event with no ADMIN license, role={}", role);
            throw new AccessDeniedException("ADMIN license is required");
        }

        EventResponse response = eventService.createEvent(request);
        log.info("event successfully create with ID={}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Long id) {
        log.debug("GET /api/events/{} - get call to event", id);

        EventResponse response = eventService.getEventById(id);

        log.debug("event with ID={} successfully shared to client", id);

        return ResponseEntity.ok(response);
    }
}
