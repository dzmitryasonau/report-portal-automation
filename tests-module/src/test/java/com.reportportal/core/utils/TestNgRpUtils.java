package com.reportportal.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.testng.IAttributes;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import com.reportportal.annotations.AutomationIssue;
import com.reportportal.annotations.Defects;
import com.reportportal.annotations.TmsId;

public final class TestNgRpUtils {

    private static final List<String> KNOWN_AUTOMATION_ISSUES = List.of("StaleElementReferenceException");

    private TestNgRpUtils() {
    }

    public static Integer[] getTestCaseIds(Method method) {
        return ArrayUtils.toObject(Optional.ofNullable(method.getAnnotation(TmsId.class)).map(tmsId -> new int[]{tmsId.value()}).orElse(new int[0]));
    }

    public static <T> T getAttribute(IAttributes attributes, String attribute) {
        return (T) attributes.getAttribute(attribute);
    }

    public static List<String> getDefects(Optional<Defects> optionalDefects){
        return optionalDefects.map(defects -> Arrays.asList(defects.value())).orElseGet(ArrayList::new);
    }

    public static List<String> getAutomationIssues(Optional<AutomationIssue> optionalDefects){
        return optionalDefects.map(defects -> Arrays.asList(defects.value())).orElseGet(ArrayList::new);
    }

    public static boolean isKnownIssue(Optional<Defects> optionalDefects, List<ITestResult> results){
        if(optionalDefects.isEmpty()){
            return false;
        }
        List<String> actualErrorMessages = results.stream()
                .filter(r -> r.getThrowable() != null)
                .map(r -> r.getThrowable().getMessage()).toList();

        return Arrays.stream(optionalDefects.get().messages())
                .anyMatch(message -> actualErrorMessages.stream().anyMatch(aM -> aM.contains(message)));
    }

    public static boolean isAutomationIssue(Optional<AutomationIssue> optionalDefects, List<ITestResult> results){
        if(optionalDefects.isEmpty()){
            return false;
        }
        List<String> actualErrorMessages = results.stream()
                .filter(r -> r.getThrowable() != null)
                .map(r -> r.getThrowable().getMessage()).toList();

        boolean isKnownAutomationIssueFromAnnotation = Arrays.stream(optionalDefects.get().messages())
                .anyMatch(message -> actualErrorMessages.stream().anyMatch(aM -> aM.contains(message)));

        boolean isKnownAutomationIssueFromList = KNOWN_AUTOMATION_ISSUES.stream()
                .anyMatch(message -> actualErrorMessages.stream().anyMatch(aM -> aM.contains(message)));

        return isKnownAutomationIssueFromAnnotation || isKnownAutomationIssueFromList;
    }

    public static <T extends Annotation> Optional<T> getOrEmptyAnnotation(ITestNGMethod testNGMethod, Class<T> annotationClass){
        T annotation = testNGMethod.getConstructorOrMethod().getMethod().getAnnotation(annotationClass);
        if(annotation != null){
            return Optional.of(annotation);
        }
        return Optional.empty();
    }
}
