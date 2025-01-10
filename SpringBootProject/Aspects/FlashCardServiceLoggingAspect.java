package com.hololitt.SpringBootProject.Aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class FlashCardServiceLoggingAspect {
    @Before("execution (* com.hololitt.SpringBootProject.services.FlashCardService.*(..))")
    public void log(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getMethod().getName();
        System.out.println("FlashCardService: method called " + methodName);

    }
}
