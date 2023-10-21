package com.reportportal.aspects;

import java.util.Optional;

import com.epam.reportportal.annotations.Step;
import com.epam.reportportal.aspect.StepRequestUtils;
import com.epam.reportportal.service.Launch;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CustomStepAspect {
    @Pointcut("@annotation(step)")
    public void withStepAnnotation(Step step) {

    }

    @Pointcut("execution(* *.*(..))")
    public void anyMethod() {

    }

    @Before(value = "anyMethod() && withStepAnnotation(step)", argNames = "joinPoint,step")
    public void startNestedStep(JoinPoint joinPoint, Step step) {
        if (step.isIgnored()) {
            return;
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        StartTestItemRQ startStepRequest = StepRequestUtils.buildStartStepRequest(signature, step, joinPoint);
        Optional.ofNullable(Launch.currentLaunch()).ifPresent(l -> l.getStepReporter().startNestedStep(startStepRequest));
    }

    @AfterReturning(value = "anyMethod() && withStepAnnotation(step)", argNames = "step")
    public void finishNestedStep(Step step) {
        if (step.isIgnored()) {
            return;
        }
        Optional.ofNullable(Launch.currentLaunch()).ifPresent(l -> l.getStepReporter().finishNestedStep());
    }

    @AfterThrowing(value = "anyMethod() && withStepAnnotation(step)", throwing = "throwable", argNames = "step,throwable")
    public void failedNestedStep(Step step, final Throwable throwable) {
        if (step.isIgnored()) {
            return;
        }
        Optional.ofNullable(Launch.currentLaunch()).ifPresent(l -> l.getStepReporter().finishNestedStep(throwable));
    }
}
