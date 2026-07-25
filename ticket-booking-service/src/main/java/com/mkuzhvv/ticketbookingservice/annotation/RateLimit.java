package com.mkuzhvv.ticketbookingservice.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    int maxRequests() default 5;

    int timeWindowSeconds() default 1;

    String key() default "";
}
