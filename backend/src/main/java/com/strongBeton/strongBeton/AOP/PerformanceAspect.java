package com.strongBeton.strongBeton.AOP;

import com.strongBeton.strongBeton.util.LogPerformance;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Timer;

@Aspect
@Component
public class PerformanceAspect {

    private LogPerformance logPerformance = new LogPerformance();

    @Before("execution(* com.strongBeton.strongBeton.service.FriendServiceImpl.getUsernames(..))")
    public void beforeGetUsernames(){
        logPerformance.startTimer();
    }

    @After("execution(* com.strongBeton.strongBeton.service.FriendServiceImpl.getUsernames(..))")
     public void afterGetUsernames(){
        logPerformance.stopTimer("getUsernames");
    }


}
