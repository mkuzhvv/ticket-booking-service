package com.mkuzhvv.ticketbookingservice.aspect;

import com.mkuzhvv.ticketbookingservice.annotation.RateLimit;
import com.mkuzhvv.ticketbookingservice.controller.exception.RateLimitExceededException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    @Around("@annotation(com.mkuzhvv.ticketbookingservice.annotation.RateLimit)")
    public Object checkRateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        // Получаем сигнатуру метода (информацию о методе: имя, параметры, аннотации)
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // Получаем сам метод
        Method method = signature.getMethod();

        // Получаем аннотацию @RateLimit с этого метода
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);

        // Если аннотации нет (теоретически невозможно, но на всякий случай)
        if (rateLimit == null) {
            return joinPoint.proceed(); // Просто вызываем метод
        }

        // Получаем ключ для Redis (идентификатор пользователя)
        String key = getClientIdentifier(rateLimit.key());

        // Формируем полный ключ для Redis: "rate_limit:bookings:user_123"
        String redisKey = "rate_limit:" + getKeyFromMethod(method) + ":" + key;

        // Получаем параметры из аннотации
        int maxRequests = rateLimit.maxRequests();
        int timeWindowSeconds = rateLimit.timeWindowSeconds();

        // Логируем проверку
        log.debug("Проверка Rate Limit: key={}, maxRequests={}, window={}s",
                redisKey, maxRequests, timeWindowSeconds);

        // Увеличиваем счётчик в Redis
        // increment() — это команда INCR в Redis (увеличивает значение на 1)
        Long currentCount = redisTemplate.opsForValue().increment(redisKey);

        // Если это первый запрос (счётчик был 0, стал 1)
        if (currentCount != null && currentCount == 1) {
            // Устанавливаем время жизни ключа (TTL)
            // expire() — это команда EXPIRE в Redis
            redisTemplate.expire(redisKey, timeWindowSeconds, TimeUnit.SECONDS);
            log.debug("Установлен TTL={} секунд для ключа {}", timeWindowSeconds, redisKey);
        }

        // Проверяем, не превышен ли лимит
        if (currentCount != null && currentCount > maxRequests) {
            // Логируем предупреждение
            log.warn("Превышен лимит запросов! Ключ={}, текущее значение={}, лимит={}",
                    redisKey, currentCount, maxRequests);

            // Выбрасываем исключение
            throw new RateLimitExceededException(
                    "Превышен лимит запросов. Попробуйте через " + timeWindowSeconds + " секунд"
            );
        }

        // Если лимит не превышен — вызываем оригинальный метод
        // proceed() — это вызов оригинального метода, который мы перехватили
        return joinPoint.proceed();
    }


    private String getClientIdentifier(String customKey) {
        // Если передан кастомный ключ — используем его
        if (customKey != null && !customKey.isEmpty()) {
            return customKey;
        }

        // Получаем текущий HTTP-запрос из контекста
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        // Если запроса нет (метод вызван не из контроллера)
        if (attributes == null) {
            return "unknown";
        }

        HttpServletRequest request = attributes.getRequest();

        // Пробуем получить заголовок X-User-Id
        String userId = request.getHeader("X-User-Id");
        if (userId != null && !userId.isEmpty()) {
            return userId;
        }

        // Если заголовка нет — берём IP-адрес
        String ipAddress = request.getRemoteAddr();
        return ipAddress != null ? ipAddress : "unknown";
    }

    private String getKeyFromMethod(Method method) {
        // Просто возвращаем имя метода
        return method.getName();
    }
}