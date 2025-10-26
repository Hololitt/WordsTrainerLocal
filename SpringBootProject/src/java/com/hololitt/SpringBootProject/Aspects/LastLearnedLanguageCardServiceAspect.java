package com.hololitt.SpringBootProject.Aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LastLearnedLanguageCardServiceAspect {

    @Before("execution (* com.hololitt.SpringBootProject.services.LastLearnedLanguageCardsService.*(..))")
    public void log(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getName();

        System.out.println("LastLearnedLanguageCardService: method execution " + methodName);
    }
}
