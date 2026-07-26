package com.mkuzhvv.ticketbookingservice.service;

import com.mkuzhvv.ticketbookingservice.model.dto.EventCreateRequest;
import com.mkuzhvv.ticketbookingservice.model.dto.EventResponse;
import com.mkuzhvv.ticketbookingservice.model.entity.Event;
import com.mkuzhvv.ticketbookingservice.controller.exception.EventNotFoundException;
import com.mkuzhvv.ticketbookingservice.mapper.EventMapper;
import com.mkuzhvv.ticketbookingservice.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private EventRepository eventRepository;

    @Override
    @Transactional
    @CacheEvict(value = "events", key = "#result.id")
    public EventResponse createEvent(EventCreateRequest request) {
        log.info("Creating new event: name={}, date={}, tickets={}", request.getName(), request.getEventDate(), request.getTotalTickets());

        try {
            Event event = eventMapper.toEntity(request);
            Event savedEvent = eventRepository.save(event);
            log.info("Event successfully create with id={}", savedEvent.getId());

            return eventMapper.toResponse(savedEvent);
        } catch (Exception ex) {
            log.error("Error while creating event: name={}", request.getName(), ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    @Cacheable(
            value = "events",
            key = "#id",
            unless = "#result != null"
    )
    public EventResponse getEventById(Long id) {
        log.debug("Event call id={}", id);

        Event event = eventRepository.findById(id).orElseThrow(() -> {
            log.warn("Event with id={} not found in data base", id);
            return new EventNotFoundException("Event with id = " + id + " not found");
        });

        log.debug("Event with id={} successfully get", id);

        return eventMapper.toResponse(event);
    }
}
