package com.reportportal.support.properties;

import org.springframework.core.io.Resource;

import com.reportportal.exceptions.InitializationException;

public final class PropertyHandlerHolder {

    private static PropertyHandlerHolder instance;
    private CustomPropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = null;
    private volatile boolean isInitCalled = false;

    private PropertyHandlerHolder() {
    }

    public static PropertyHandlerHolder getInstance() {
        if (instance == null) {
            synchronized (PropertyHandlerHolder.class) {
                if (instance == null) {
                    instance = new PropertyHandlerHolder();
                }
            }
        }
        return instance;
    }

    public PropertyHandlerHolder init(Resource[] resources) {
        if (!isInitCalled) {
            synchronized (PropertyHandlerHolder.class) {
                if (!isInitCalled) {
                    propertySourcesPlaceholderConfigurer = new CustomPropertySourcesPlaceholderConfigurer();
                    propertySourcesPlaceholderConfigurer.setLocations(resources);
                    propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
                    isInitCalled = true;
                }
            }
        }
        return this;
    }

    public CustomPropertySourcesPlaceholderConfigurer getPropertyConfigurer() {
        if (!isInitCalled) {
            throw new InitializationException("The 'init' method should be called once per initialization.");
        }
        return propertySourcesPlaceholderConfigurer;
    }

}
