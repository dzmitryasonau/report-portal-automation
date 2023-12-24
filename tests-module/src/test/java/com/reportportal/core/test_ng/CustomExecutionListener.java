package com.reportportal.core.test_ng;

import com.reportportal.config.SpringDomainConfig;
import com.reportportal.service.TestNgRpUtils;
import com.reportportal.support.CustomPropertySourcesPlaceholderConfigurer;
import com.reportportal.support.PropertyHandlerHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomExecutionListener implements IMethodInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExecutionListener.class);
    private final CustomPropertySourcesPlaceholderConfigurer placeholderConfigurer;

    public CustomExecutionListener() {
        placeholderConfigurer = PropertyHandlerHolder.getInstance().init(SpringDomainConfig.getPropertiesResources())
                .getPropertyConfigurer();
    }

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        List<IMethodInstance> filteredMethods = new ArrayList<>(methods);

        String singleTestId = placeholderConfigurer.get("single.test.id");
        boolean isSingleTestRun = Objects.nonNull(singleTestId) && (!singleTestId.isEmpty() || !singleTestId.isBlank());
        LOGGER.info("<<Filter tests by conditions: isSingleTestRun={}", isSingleTestRun);

        filteredMethods.removeIf(mi ->
        {
            Method method = mi.getMethod().getConstructorOrMethod().getMethod();
            if(isSingleTestRun){
                List<Integer> testCaseIds = Arrays.asList(TestNgRpUtils.getTestCaseIds(method));
                return !testCaseIds.contains(getCaseId(singleTestId));
            }
            return false;
        });

        if (filteredMethods.isEmpty()) {
            LOGGER.info("!!NO TESTS TO RUN BY SPECIFIED CONDITIONS!!");
        } else {
            String methodsToRun = filteredMethods.stream()
                    .map(m -> m.getMethod().getRealClass().getSimpleName() + "#" + m.getMethod().getMethodName())
                    .collect(Collectors.joining(","));
            LOGGER.info("TESTS TO RUN:{}", methodsToRun);
        }
        return filteredMethods;
    }

   private Integer getCaseId(String singleTestId) {
        if (singleTestId.startsWith("c") || singleTestId.startsWith("C")) {
            singleTestId = singleTestId.substring(1);
        }
        return Integer.parseInt(singleTestId);
    }
}
