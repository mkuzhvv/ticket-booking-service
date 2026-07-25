package com.mkuzhvv.ticketbookingservice.controller.exception;

public class NotEnoughTicketsException extends RuntimeException {
    public NotEnoughTicketsException(String message) {
        super(message);
    }
}
