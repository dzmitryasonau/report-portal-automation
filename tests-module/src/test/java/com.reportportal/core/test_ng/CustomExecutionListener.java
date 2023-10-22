package com.reportportal.core.test_ng;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.reportportal.config.SpringDomainConfig;
import com.reportportal.meta.TestCategory;
import com.reportportal.support.properties.CustomPropertySourcesPlaceholderConfigurer;
import com.reportportal.support.properties.PropertyHandlerHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.reportportal.core.utils.TestNgRpUtils;

public class CustomExecutionListener implements IMethodInterceptor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExecutionListener.class);
    private final CustomPropertySourcesPlaceholderConfigurer placeholderConfigurer;

    public CustomExecutionListener()
    {
        placeholderConfigurer = PropertyHandlerHolder.getInstance().init(SpringDomainConfig.getPropertiesResources())
                .getPropertyConfigurer();
    }

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context)
    {
        List<IMethodInstance> filteredMethods = new ArrayList<>(methods);

        String category = placeholderConfigurer.get("category");
        String singleTestId = placeholderConfigurer.get("single.test.id");
        boolean isSingleTestRun = Objects.nonNull(singleTestId) && (!singleTestId.isEmpty() || !singleTestId.isBlank());
        boolean isFilterByCategory = Objects.nonNull(category) && (!category.isEmpty() || !category.isBlank());
        LOGGER.info("<<Filter tests by conditions: isSingleTestRun={}, isFilterByCategory={}>>", isSingleTestRun,
                isFilterByCategory);

        filteredMethods.removeIf(mi ->
        {
            Method method = mi.getMethod().getConstructorOrMethod().getMethod();
            if (isSingleTestRun)
            {
                List<Integer> testCaseIds = Arrays.asList(TestNgRpUtils.getTestCaseIds(method));
                return !testCaseIds.contains(getCaseId(singleTestId));
            }
            List<String> groups = Arrays.asList(method.getAnnotation(Test.class).groups());
            if (isFilterByCategory)
            {
                if (!TestCategory.REGRESSION.equalsIgnoreCase(category))
                {
                    return !groups.contains(category);
                }
            }
            return true;
        });

        if (filteredMethods.isEmpty())
        {
            LOGGER.info("!!NO TESTS TO RUN BY SPECIFIED CONDITIONS!!");
        }
        else
        {
            String methodsToRun = filteredMethods.stream()
                    .map(m -> m.getMethod().getRealClass().getSimpleName() + "#" + m.getMethod().getMethodName())
                    .collect(Collectors.joining(","));
            LOGGER.info("TESTS TO RUN:{}", methodsToRun);
        }
        return filteredMethods;
    }

    private Integer getCaseId(String singleTestId)
    {
        if (singleTestId.startsWith("c") || singleTestId.startsWith("C"))
        {
            singleTestId = singleTestId.substring(1);
        }
        return Integer.parseInt(singleTestId);
    }
}
