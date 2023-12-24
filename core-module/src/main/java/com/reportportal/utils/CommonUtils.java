package com.reportportal.utils;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vavr.control.Try;

public final class CommonUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

    private CommonUtils()
    {
    }

    public static Integer getRandomInteger(int max, int min)
    {
        return new Random().nextInt(min, max);
    }

    public static Integer getRandomInteger(int max)
    {
        return getRandomInteger(max, 0);
    }
    public static void sleep(long millis) {
        sleep(millis, true);
    }

    public static void sleep(long millis, boolean isLog) {
        if (isLog) {
            LOGGER.warn("Thread sleep for '{}' millis", millis);
        }
        Try.run(() -> Thread.sleep(millis));
    }
}
