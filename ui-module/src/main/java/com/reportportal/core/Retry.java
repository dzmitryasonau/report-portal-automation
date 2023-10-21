package com.reportportal.core;

import com.github.rholder.retry.*;
import com.reportportal.exceptions.AutomationException;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Retry {
    private static final Logger LOGGER = LoggerFactory.getLogger(Retry.class);
    private static final long REPEAT_TIMEOUT = 90000;
    private static final long REPEAT_DELAY = 3000;
    private static final StopStrategy defaultStopStrategy = StopStrategies.stopAfterDelay(REPEAT_TIMEOUT, TimeUnit.MILLISECONDS);
    private static final WaitStrategy defaultWaitStrategy = WaitStrategies.fixedWait(REPEAT_DELAY, TimeUnit.MILLISECONDS);

    public static <T> T run(Predicate<T> predicate, Supplier<T> supplier) {
        return run(predicate, supplier, defaultStopStrategy, defaultWaitStrategy);
    }

    public static <T> T run(Predicate<T> predicate, StopStrategy stopStrategy, WaitStrategy waitStrategy) {
        return run(predicate, () -> {
            return null;
        }, stopStrategy, waitStrategy);
    }

    public static <T> T run(Predicate<T> predicate, Supplier<T> supplier, StopStrategy stopStrategy, WaitStrategy waitStrategy) {
        Retryer<T> retry = (Retryer<T>) RetryerBuilder
                .newBuilder()
                .retryIfRuntimeException()
                .retryIfResult(entity -> {
                    return Objects.equals(predicate.test((T) entity), Boolean.FALSE);
                })
                .retryIfException()
                .withStopStrategy(stopStrategy)
                .withWaitStrategy(waitStrategy)
                .build();
        return call(retry, supplier);
    }

    private static <T> T call(Retryer retry, Supplier<T> supplier) {
        return (T) Try.of(() -> {
                    Objects.requireNonNull(supplier);
                    return retry.call(supplier::get);
                })
                .andFinally(() -> LOGGER.info("Request retrying has been finished"))
                .getOrElseThrow(() -> new AutomationException("Exception during retry performing"));
    }
}
