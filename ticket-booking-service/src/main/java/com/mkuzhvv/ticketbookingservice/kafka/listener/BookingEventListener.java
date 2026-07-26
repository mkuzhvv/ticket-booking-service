package com.mkuzhvv.ticketbookingservice.kafka.listener;

import com.mkuzhvv.ticketbookingservice.model.dto.BookingEventMessage;
import com.mkuzhvv.ticketbookingservice.kafka.event.BookingCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingEventListener {

    @Autowired
    private KafkaTemplate<String, BookingEventMessage> kafkaTemplate;
    private static final String BOOKING_EVENTS_TOPIC = "booking-events";

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBookingCreated(BookingCreatedEvent event) {
        BookingEventMessage message = event.getMessage();

        log.info("Sending event to Kafka: {}", message);

        kafkaTemplate.send(BOOKING_EVENTS_TOPIC, message.getUserId(), message);

        log.info("Event successfully send to Kafka with messageId: {}", message.getMessageId());
    }
}
