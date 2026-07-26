package com.mkuzhvv.ticketbookingservice.service;

import com.mkuzhvv.ticketbookingservice.model.dto.BookingRequest;
import com.mkuzhvv.ticketbookingservice.model.dto.BookingResponse;

public interface BookingService {
    BookingResponse bookTickets(BookingRequest request);
}
