package com.reportportal.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class GenericBuilder<T> {

    private final Supplier<T> instanceSupplier;
    private final List<Consumer<T>> instanceModifiers = new ArrayList<>();

    private GenericBuilder(final Supplier<T> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
    }

    public static <T> GenericBuilder<T> of(final Supplier<T> constructor) {
        return new GenericBuilder<>(constructor);
    }

    public GenericBuilder<T> with(Consumer<T> setter) {
        instanceModifiers.add(setter);
        return this;
    }

    public T build() {
        final T instance = instanceSupplier.get();
        instanceModifiers.forEach(instanceModifier -> instanceModifier.accept(instance));
        return instance;
    }
}
