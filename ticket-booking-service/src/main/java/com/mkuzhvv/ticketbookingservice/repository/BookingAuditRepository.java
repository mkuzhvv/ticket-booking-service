package com.mkuzhvv.ticketbookingservice.repository;

import com.mkuzhvv.ticketbookingservice.model.entity.BookingAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingAuditRepository extends JpaRepository<BookingAudit, Long> {
}