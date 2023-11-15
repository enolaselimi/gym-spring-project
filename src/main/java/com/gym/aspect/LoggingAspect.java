package com.gym.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class LoggingAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    HttpServletRequest httpServletRequest;

    @Before("com.gym.aspect.JoinPointCustom.serviceLayerPointcut()")
    public void beforeLogAspect(JoinPoint joinPoint){
        logger.info("BEFORE - Beginning method: "+joinPoint.getSignature().getName()+" with endpoint: "+httpServletRequest.getRequestURI());
    }

    @After("com.gym.aspect.JoinPointCustom.serviceLayerPointcut()")
    public void afterLogAspect(JoinPoint joinPoint){
        logger.info("AFTER - After calling: "+joinPoint.getSignature().getName()+" with endpoint: "+httpServletRequest.getRequestURI());
    }

    @AfterReturning("com.gym.aspect.JoinPointCustom.serviceLayerPointcut()")
    public void afterReturningAspect(JoinPoint joinPoint){
        logger.info("AFTER RETURN - Method {} with endpoint {} completed",joinPoint.getSignature().getName(), httpServletRequest.getRequestURI());
    }

    @AfterThrowing(value="com.gym.aspect.JoinPointCustom.dataLayerPointcut()", throwing="exception")
    public void afterThrowingAspect(JoinPoint joinPoint, Exception exception){
        logger.info("AFTER THROWING - Method {} returned with {}",joinPoint.getSignature().getName(), exception);
    }

    @Around("com.gym.aspect.JoinPointCustom.trackExecutionTimePointcut()")
    public Object executionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        long start = System.currentTimeMillis();
        var proceeding = proceedingJoinPoint.proceed();
        long executionTimeTaken = System.currentTimeMillis() - start;
        logger.info("AROUND - Method {} took {} ms to execute",proceedingJoinPoint.getSignature().getName(), executionTimeTaken);
        return proceeding;
    }
}
