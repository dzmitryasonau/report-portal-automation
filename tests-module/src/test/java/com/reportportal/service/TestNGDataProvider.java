package com.reportportal.service;

import java.util.List;

import com.reportportal.models.launch.api.Attribute;
import com.reportportal.utils.CommonUtils;

import org.testng.annotations.DataProvider;

public class TestNGDataProvider
{
    private static final String ATTRIBUTE_PREFIX = "Atr: ";
    private static final String ATTRIBUTE_VALUE = "value";
    private static final Integer MAX_INDEX = 10000;

    @DataProvider(name = "suites")
    public static Object[][] getSuites()
    {
        return new Object[][] { { 8931843 }, { 8931844 }, { 8931845 }, { 8931846 }, { 8931847 } };
    }

    @DataProvider(name = "attributes")
    public static Object[][] getAttributes()
    {
        return new Object[][] { { 8931843,
                List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX), ATTRIBUTE_VALUE)) },
                { 8931844, List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX),
                                ATTRIBUTE_VALUE),
                        new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX), ATTRIBUTE_VALUE)) },
                { 8931845, List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX),
                        ATTRIBUTE_VALUE)) }, { 8931846,
                List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX), ATTRIBUTE_VALUE)) },
                { 8931847, List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX),
                        ATTRIBUTE_VALUE)) } };
    }

    @DataProvider(name = "launches", parallel = true)
    public static Object[][] getLaunches()
    {
        return new Object[][] { { 8931843, "FAILED" }, { 8931844, "FAILED" }, { 8931845, "FAILED" },
                { 8931846, "FAILED" }, { 8931847, "INTERRUPTED" } };
    }
}
