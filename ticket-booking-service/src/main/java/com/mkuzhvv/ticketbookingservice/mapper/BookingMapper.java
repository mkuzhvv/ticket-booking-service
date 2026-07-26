package com.mkuzhvv.ticketbookingservice.mapper;

import com.mkuzhvv.ticketbookingservice.model.dto.BookingRequest;
import com.mkuzhvv.ticketbookingservice.model.dto.BookingResponse;
import com.mkuzhvv.ticketbookingservice.model.entity.Booking;
import com.mkuzhvv.ticketbookingservice.model.entity.BookingStatus;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public Booking toEntity(BookingRequest request) {
        return Booking.builder()
                .eventId(request.getEventId())
                .userId(request.getUserId())
                .ticketCount(request.getTicketCount())
                .status(BookingStatus.CONFIRMED)
                .build();
    }

    public BookingResponse toResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .eventId(booking.getEventId())
                .userId(booking.getUserId())
                .ticketCount(booking.getTicketCount())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
