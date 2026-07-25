package com.mkuzhvv.ticketbookingservice.controller.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
