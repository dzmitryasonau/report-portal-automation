package com.reportportal.utils;

import java.util.Random;

public final class CommonUtils
{

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
}
