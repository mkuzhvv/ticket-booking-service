package com.mkuzhvv.ticketbookingservice.controller.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) {
        super(message);
    }
}