package com.mkuzhvv.ticketbookingservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EventCreateRequest {

    @NotBlank(message = "Event name shouldn't be empty")
    private String name;

    @NotNull(message = "Date is required")
    @Future(message = "Event date must be in future")
    private LocalDateTime eventDate;

    @NotNull(message = "Total tickets is required")
    @Positive(message = "Total tickets must >= 0")
    private Integer totalTickets;

    @NotNull(message = "price is required")
    @Min(value = 0, message = "price must be positive")
    private BigDecimal price;
}
