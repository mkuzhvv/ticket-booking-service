package com.mkuzhvv.ticketbookingservice.kafka.consumer;

import com.mkuzhvv.ticketbookingservice.model.dto.BookingEventMessage;
import com.mkuzhvv.ticketbookingservice.model.entity.BookingAudit;
import com.mkuzhvv.ticketbookingservice.repository.BookingAuditRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookingConsumer {

    @Autowired
    private BookingAuditRepository auditRepository;
    private static final String BOOKING_EVENTS_TOPIC = "booking-events";

    @KafkaListener(topics = BOOKING_EVENTS_TOPIC, groupId = "ticket-booking-group")
    public void consumeBookingEvent(BookingEventMessage message) {
        log.info("got message from Kafka: {}", message);
        processBookingEvent(message);
    }

    private void processBookingEvent(BookingEventMessage message) {
        log.info("Booking .... : {}", message.getMessageId());

        try {
            BookingAudit audit = BookingAudit.builder()
                    .eventId(message.getEventId())
                    .eventName(message.getEventName())
                    .userId(message.getUserId())
                    .ticketCount(message.getTicketCount())
                    .totalPrice(message.getTotalPrice().doubleValue())
                    .messageId(message.getMessageId())
                    .createdAt(message.getCreatedAt())
                    .status(BookingAudit.AuditStatus.SUCCESS)
                    .build();

            auditRepository.save(audit);

            log.info("Audit saved for event: {}", message.getMessageId());

            // Здесь может быть реальная логика:
            // - Отправка email пользователю
            // - Генерация PDF-билета
            // - Обновление статистики
            // sendEmailNotification(message);

        } catch (Exception e) {
            log.error("Error while booking .... {}", message.getMessageId(), e);

            BookingAudit audit = BookingAudit.builder()
                    .eventId(message.getEventId())
                    .eventName(message.getEventName())
                    .userId(message.getUserId())
                    .ticketCount(message.getTicketCount())
                    .totalPrice(message.getTotalPrice().doubleValue())
                    .messageId(message.getMessageId())
                    .createdAt(message.getCreatedAt())
                    .status(BookingAudit.AuditStatus.FAILED)
                    .errorMessage(e.getMessage())
                    .build();

            auditRepository.save(audit);
        }
    }
}
