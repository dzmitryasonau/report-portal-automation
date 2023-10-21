package com.reportportal.support.properties;

import java.util.Properties;

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.util.CollectionUtils;

import io.vavr.control.Try;

public class CustomPropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {

    @Override
    public Properties mergeProperties() {
        Properties propertiesFromFiles = Try.of(super::mergeProperties).get();
        Properties envProperties = new Properties();
        envProperties.putAll(System.getenv());
        Properties systemProperties = System.getProperties();
        Properties resultProperties = new Properties();
        CollectionUtils.mergePropertiesIntoMap(propertiesFromFiles, resultProperties);
        CollectionUtils.mergePropertiesIntoMap(envProperties, resultProperties);
        CollectionUtils.mergePropertiesIntoMap(systemProperties, resultProperties);
        return resultProperties;
    }

    public String get(String key) {
        return mergeProperties().getProperty(key);
    }
}
