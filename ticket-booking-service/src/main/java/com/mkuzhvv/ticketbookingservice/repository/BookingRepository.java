package com.mkuzhvv.ticketbookingservice.repository;

import com.mkuzhvv.ticketbookingservice.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    long countByEventIdAndUserId(Long eventId, String userId);
}
