package com.reportportal.config;

import io.vavr.control.Try;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.util.Pair;

import java.util.Optional;
import java.util.Properties;

public class ResourcesData {
    private static final String LOCAL_PROPERTIES_FILE_NAME = "local.properties";
    private static final Optional<Pair<Properties, ClassPathResource>> PROPERTIES_DATA;

    private ResourcesData() {
    }

    static {
        PROPERTIES_DATA = getLocalProperties();
    }

    public static Optional<Pair<Properties, ClassPathResource>> getLocalProperties() {
        ClassPathResource resource = new ClassPathResource(LOCAL_PROPERTIES_FILE_NAME);
        return Try.withResources(resource::getInputStream).of(inputStream ->
        {
            Properties props = new Properties();
            props.load(inputStream);
            return Pair.of(props, resource);
        }).toJavaOptional();
    }
}
