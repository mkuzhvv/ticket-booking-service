package com.mkuzhvv.ticketbookingservice.repository;

import com.mkuzhvv.ticketbookingservice.entity.Event;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Это блокирует строку в БД на время транзакции
    // Если два юзера одновременно пытаются купить последний билет
    // второй запрос будет ждать пока первый завершится и увидит уже обновленное количество билетов
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Event e WHERE e.id = :id")
    Optional<Event> findByIdWithLock(@Param("id") Long id);
}
