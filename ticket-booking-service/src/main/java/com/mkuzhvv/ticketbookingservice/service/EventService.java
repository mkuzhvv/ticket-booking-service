package com.mkuzhvv.ticketbookingservice.service;

import com.mkuzhvv.ticketbookingservice.model.dto.EventCreateRequest;
import com.mkuzhvv.ticketbookingservice.model.dto.EventResponse;

public interface EventService {
    EventResponse createEvent(EventCreateRequest request);
    EventResponse getEventById(Long id);
}
