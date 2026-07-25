package com.mkuzhvv.ticketbookingservice.mapper;

import com.mkuzhvv.ticketbookingservice.dto.EventCreateRequest;
import com.mkuzhvv.ticketbookingservice.dto.EventResponse;
import com.mkuzhvv.ticketbookingservice.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    public Event toEntity(EventCreateRequest request) {
        return Event.builder()
                .name(request.getName())
                .eventDate(request.getEventDate())
                .totalTickets(request.getTotalTickets())
                .availableTickets(request.getTotalTickets())
                .price(request.getPrice())
                .build();
    }

    public EventResponse toResponse(Event event)  {
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .eventDate(event.getEventDate())
                .totalTickets(event.getTotalTickets())
                .availableTickets(event.getAvailableTickets())
                .price(event.getPrice())
                .createdAt(event.getCreatedAt())
                .build();
    }
}
