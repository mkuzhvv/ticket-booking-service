package com.mkuzhvv.ticketbookingservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId;
    private String eventName;
    private String userId;
    private Integer ticketCount;
    private Double totalPrice;
    private String messageId;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private AuditStatus status;

    //возможная ошибка при обработке
    private String errorMessage;

    public enum AuditStatus {
        PENDING,
        SUCCESS,
        FAILED
    }
}
