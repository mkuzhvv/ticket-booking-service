package com.mkuzhvv.ticketbookingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TicketBookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketBookingServiceApplication.class, args);
    }

}
