package com.strongBeton.strongBeton.aop;

import com.strongBeton.strongBeton.util.LogPerformance;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {

    private LogPerformance logPerformance = new LogPerformance();

    @Before("execution(* com.strongBeton.strongBeton.service.SocialServices.FriendServiceImpl.getUsernames(..))")
    public void beforeGetUsernames(){
        logPerformance.startTimer();
    }

    @After("execution(* com.strongBeton.strongBeton.service.SocialServices.FriendServiceImpl.getUsernames(..))")
     public void afterGetUsernames(){
        logPerformance.stopTimer("getUsernames");
    }


}
