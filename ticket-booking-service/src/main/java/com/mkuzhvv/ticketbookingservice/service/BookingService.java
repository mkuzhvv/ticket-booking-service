package com.mkuzhvv.ticketbookingservice.service;

import com.mkuzhvv.ticketbookingservice.dto.BookingRequest;
import com.mkuzhvv.ticketbookingservice.dto.BookingResponse;

public interface BookingService {
    BookingResponse bookTickets(BookingRequest request);
}
