package com.reportportal.utils;

import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

public final class CommonUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);
    private static final SecureRandom RANDOM = new SecureRandom();

    private CommonUtils()
    {
    }

    public static Integer getRandomInteger(int max, int min)
    {
        return RANDOM.nextInt(min, max);
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
