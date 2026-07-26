package com.mkuzhvv.ticketbookingservice.kafka.event;

import com.mkuzhvv.ticketbookingservice.model.dto.BookingEventMessage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BookingCreatedEvent extends ApplicationEvent {

    private final BookingEventMessage message;

    public BookingCreatedEvent(Object source, BookingEventMessage message) {
        super(source);
        this.message = message;
    }
}
