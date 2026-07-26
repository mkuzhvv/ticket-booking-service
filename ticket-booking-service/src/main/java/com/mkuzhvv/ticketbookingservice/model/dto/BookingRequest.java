package com.mkuzhvv.ticketbookingservice.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {

    @NotNull(message = "ID is required")
    private Long eventId;

    @NotBlank(message = "ID is required")
    private String userId;

    @NotNull(message = "ticketCount is required")
    @Min(value = 1, message = "Min ticket count is 1")
    private Integer ticketCount;
}
