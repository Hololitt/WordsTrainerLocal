package com.hololitt.SpringBootProject.Aspects;

import com.hololitt.SpringBootProject.models.LanguageCard;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Aspect
public class WordsTrainerServiceLoggingAspect {
    @Before("execution (* com.hololitt.SpringBootProject.services.WordsTrainerService.*(..))")
public void log(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getName();
        System.out.println("Words Trainer Service: method called " + methodName);
    }
@AfterReturning(pointcut = "execution (* com.hololitt.SpringBootProject.services.WordsTrainerService.*(..))",
        returning = "languageCardList")
    public void logReturningLanguageCards(List<LanguageCard> languageCardList){
    System.out.println("List of language cards has size " + languageCardList.size());
}

}
