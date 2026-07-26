package com.mkuzhvv.ticketbookingservice.service;

import com.mkuzhvv.ticketbookingservice.controller.exception.EventNotFoundException;
import com.mkuzhvv.ticketbookingservice.controller.exception.NotEnoughTicketsException;
import com.mkuzhvv.ticketbookingservice.model.dto.BookingRequest;
import com.mkuzhvv.ticketbookingservice.model.dto.BookingResponse;
import com.mkuzhvv.ticketbookingservice.model.entity.Booking;
import com.mkuzhvv.ticketbookingservice.model.entity.Event;
import com.mkuzhvv.ticketbookingservice.model.dto.BookingEventMessage;
import com.mkuzhvv.ticketbookingservice.mapper.BookingMapper;
import com.mkuzhvv.ticketbookingservice.kafka.event.BookingCreatedEvent;
import com.mkuzhvv.ticketbookingservice.repository.BookingRepository;
import com.mkuzhvv.ticketbookingservice.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingResponse bookTickets(BookingRequest request) {
        log.info("Booking start: eventId={}, userId={}, tickets={}", request.getEventId(), request.getUserId(), request.getTicketCount());

        //находим ивент + блокируем
        Event event = eventRepository.findByIdWithLock(request.getEventId())
                .orElseThrow(() -> {
                    log.warn("Event not foud: ID={}", request.getEventId());
                    return new EventNotFoundException("Event not foud");
                });


        //проверка кол-ва билетов
        if (event.getAvailableTickets() < request.getTicketCount()) {
            log.warn("Not enough tickets: available={} required={}", event.getAvailableTickets(), request.getTicketCount());
            throw new NotEnoughTicketsException("Not enough tickets");
        }

        //создаем бронирования
        Booking booking = bookingMapper.toEntity(request);

        BigDecimal totalPrice = event.getPrice().multiply(BigDecimal.valueOf(request.getTicketCount()));
        booking.setTotalPrice(totalPrice);

        event.setAvailableTickets(event.getAvailableTickets() - request.getTicketCount());

        Booking savedBooking = bookingRepository.save(booking);
        log.info("Booking successful: bookingId={} eventId={} totalPrice={}", savedBooking.getId(), savedBooking.getEventId(), totalPrice);

        //cоздаём DTO для Kafka
        BookingEventMessage message = BookingEventMessage.builder()
                .messageId(UUID.randomUUID().toString())
                .eventId(event.getId())
                .eventName(event.getName())
                .userId(request.getUserId())
                .ticketCount(request.getTicketCount())
                .totalPrice(booking.getTotalPrice())
                .createdAt(LocalDateTime.now())
                .build();

        //публикуем спринг событие
        applicationEventPublisher.publishEvent(new BookingCreatedEvent(this, message));

        return bookingMapper.toResponse(savedBooking);
    }
}
