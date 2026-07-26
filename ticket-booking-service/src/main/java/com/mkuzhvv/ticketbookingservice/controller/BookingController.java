package com.mkuzhvv.ticketbookingservice.controller;

import com.mkuzhvv.ticketbookingservice.annotation.RateLimit;
import com.mkuzhvv.ticketbookingservice.model.dto.BookingRequest;
import com.mkuzhvv.ticketbookingservice.model.dto.BookingResponse;
import com.mkuzhvv.ticketbookingservice.service.BookingService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
@Slf4j
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    @RateLimit(maxRequests = 5, timeWindowSeconds = 1)
    public ResponseEntity<BookingResponse> bookTickets(@Valid @RequestBody BookingRequest request) {
        log.info("POST /api/bookings - booking request from user with userId={}", request.getUserId());

        BookingResponse response = bookingService.bookTickets(request);

        log.info("Booking success id={}", response.getId());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
