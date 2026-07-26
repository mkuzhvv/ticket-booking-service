package com.mkuzhvv.ticketbookingservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingEventMessage {
    private String messageId;

    private Long eventId;

    private String eventName;

    private String userId;

    private Integer ticketCount;

    private BigDecimal totalPrice;

    private LocalDateTime createdAt;
}
