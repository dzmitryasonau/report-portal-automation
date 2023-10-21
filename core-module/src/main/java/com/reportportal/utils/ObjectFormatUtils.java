package com.reportportal.utils;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.bean.CsvToBeanBuilder;

import io.vavr.control.Try;

public final class ObjectFormatUtils
{

    private ObjectFormatUtils() {
    }

    public static Object[] decorateToJsonObjectTreeIfPossible(Object[] args) {
        return Arrays.stream(args).map(ObjectFormatUtils::wrapToJsonTreeIfPossible).toArray(Object[]::new);
    }

    public static <T> String wrapToJsonTreeIfPossible(T object) {
        return Try.of(() -> new Gson().toJsonTree(object).toString()).getOrElse(Objects.isNull(object) ? "" : object.getClass().getSimpleName());
    }

    public static String toPrettyJson(Object obj) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }

    public static <T> List<T> fromJsonToList(String json, TypeToken<ArrayList<T>> typeToken) {
        Type listType = typeToken.getType();
        return new Gson().fromJson(json, listType);
    }

    public static <T> List<T> fromCsv(File file, Class<T> clazz) {
        return Try.of(() -> new CsvToBeanBuilder(new FileReader(file)).withType(clazz).withSkipLines(1).build().parse()).getOrElse(new ArrayList<T>());
    }

    public static String joinByComma(Object[] args) {
        return Arrays.stream(args).map(Object::toString).collect(Collectors.joining(", "));
    }

    public static Double roundDouble(Double value, Integer scale) {
        return new BigDecimal(String.valueOf(value)).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static Double convertDoubleToDecimalFormat(Double value) {
        return Double.parseDouble(new DecimalFormat("#.#").format(value));
    }

    public static String formatMoneyUSLocale(Double moneyValue) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
        return currencyFormatter.format(moneyValue);
    }

    public static String formatMoneyEULocale(Double moneyValue) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
        Locale locale = new Locale("en", "IE");
        String symbol = Currency.getInstance(locale).getSymbol(locale);
        formatter.setGroupingUsed(true);
        formatter.setPositivePrefix(symbol + " ");
        formatter.setNegativePrefix("-" + symbol + " ");
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(moneyValue);
    }

    public static String formatMoneyLocaleWithoutDecimals(String moneyValue) {
        String[] parts = moneyValue.contains(" - ") ? moneyValue.split(" - ") : new String[]{moneyValue};
        DecimalFormat formatter = new DecimalFormat("#,###");
        List<String> modifiedParts = Arrays.stream(parts).map(part ->
                        formatter.format(Double.parseDouble(part.replace("$", "").replaceAll(",", ""))).replace(",", " "))
                .toList();
        StringBuilder result = new StringBuilder("$");
        for (int i = 0; i < modifiedParts.size(); i++) {
            result.append(modifiedParts.get(i));
            if (i != modifiedParts.size() - 1) {
                result.append(" - $");
            }
        }
        return result.toString();
    }

    public static List<String> removeASCIISymbols(List<String> consoleOutput) {
        return consoleOutput.stream().map(c -> c.replaceAll("\u001B\\[[;\\d]*[ -/]*[@-~]", "")).collect(Collectors.toList());
    }

    public static String replaceWithStar(String input) {
        return input.chars().mapToObj(i -> "*").collect(Collectors.joining());
    }
}
