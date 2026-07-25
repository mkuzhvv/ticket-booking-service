package com.mkuzhvv.ticketbookingservice.service;

import com.mkuzhvv.ticketbookingservice.dto.EventCreateRequest;
import com.mkuzhvv.ticketbookingservice.dto.EventResponse;

public interface EventService {
    EventResponse createEvent(EventCreateRequest request);
    EventResponse getEventById(Long id);
}
