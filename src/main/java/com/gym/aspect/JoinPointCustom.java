package com.gym.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class JoinPointCustom {
    @Pointcut("execution(* com.gym.service.*.*(..))")
    public void serviceLayerPointcut(){}

    @Pointcut("execution(* com.gym.repository.*.*(..))")
    public void dataLayerPointcut(){}

    @Pointcut("@annotation(com.gym.aspect.TrackExecutionTime)")
    public void trackExecutionTimePointcut(){}

}
