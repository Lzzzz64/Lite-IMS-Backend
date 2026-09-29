package org.example.c.backend.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LogAspect {
    @Pointcut("execution(* org.example.c.backend.controller..*.*(..))")
    public void logPointCut() {}

    @Around("logPointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 1. 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            log.info("Request: {} {} - Args: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    Arrays.toString(joinPoint.getArgs()));
        }

        // 2. 执行目标方法
        Object result = joinPoint.proceed();

        // 3. 计算耗时
        long timeTaken = System.currentTimeMillis() - startTime;
        log.info("Response: {} ms", timeTaken);

        return result;
    }
}
