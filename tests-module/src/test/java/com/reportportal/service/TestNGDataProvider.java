package com.reportportal.service;

import java.util.List;

import com.reportportal.models.launch.Attribute;
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
        return new Object[][] { { 6262801 }, { 6262802 }, { 6262803 }, { 6262804 }, { 6262805 } };
    }

    @DataProvider(name = "attributes")
    public static Object[][] getAttributes()
    {
        return new Object[][] { { 6262801,
                List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX), ATTRIBUTE_VALUE)) },
                { 6262802, List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX),
                                ATTRIBUTE_VALUE),
                        new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX), ATTRIBUTE_VALUE)) },
                { 6262803, List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX),
                        ATTRIBUTE_VALUE)) }, { 6262804,
                List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX), ATTRIBUTE_VALUE)) },
                { 6262805, List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX),
                        ATTRIBUTE_VALUE)) } };
    }

    @DataProvider(name = "launches", parallel = true)
    public static Object[][] getLaunches()
    {
        return new Object[][] { { 6262801, "FAILED" }, { 6262802, "FAILED" }, { 6262803, "FAILED" },
                { 6262804, "FAILED" }, { 6262805, "PASSED" } };
    }
}
