package com.mkuzhvv.ticketbookingservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class EventResponse {
    private Long id;
    private String name;
    private LocalDateTime eventDate;
    private Integer totalTickets;
    private Integer availableTickets;
    private BigDecimal price;
    private LocalDateTime createdAt;
}
