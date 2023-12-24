package com.reportportal.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.vavr.control.Try;

import java.util.Objects;
import java.util.stream.Collectors;

public final class ObjectFormatUtils
{

    private ObjectFormatUtils() {
    }

    public static <T> String wrapToJsonTreeIfPossible(T object) {
        return Try.of(() -> new Gson().toJsonTree(object).toString()).getOrElse(Objects.isNull(object) ? "" : object.getClass().getSimpleName());
    }

    public static String toPrettyJson(Object obj) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(obj);
    }

    public static String replaceWithStar(String input) {
        return input.chars().mapToObj(i -> "*").collect(Collectors.joining());
    }
}
