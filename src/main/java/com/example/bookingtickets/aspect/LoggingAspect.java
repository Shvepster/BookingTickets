package com.example.bookingtickets.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

  @Around("execution(* com.example.bookingtickets.service.*.*(..))")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();

    Object proceed = joinPoint.proceed();

    long executionTime = System.currentTimeMillis() - start;

    log.info("AOP [Timer]: Метод {} выполнен за {} мс",
        joinPoint.getSignature().toShortString(),
        executionTime);

    return proceed;
  }
}