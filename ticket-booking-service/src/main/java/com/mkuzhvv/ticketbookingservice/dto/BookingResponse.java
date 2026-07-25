package com.mkuzhvv.ticketbookingservice.dto;

import com.mkuzhvv.ticketbookingservice.entity.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private Long eventId;
    private String userId;
    private Integer ticketCount;
    private BigDecimal totalPrice;
    private BookingStatus status;
    private LocalDateTime createdAt;
}
