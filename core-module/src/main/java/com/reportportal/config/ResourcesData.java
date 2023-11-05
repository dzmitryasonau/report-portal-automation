package com.reportportal.config;

import java.util.Optional;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.util.Pair;

import io.vavr.control.Try;

public class ResourcesData
{
    public static final String COMMON_PROPERTIES_FILE_NAME = "common.properties";
    private static final String TEST_DATA_FOLDER_NAME = "test_data";
    private static final String LOCAL_PROPERTIES_FILE_NAME = "local.properties";
    private static final Optional<Pair<Properties, ClassPathResource>> propertiesData;

    static
    {
        propertiesData = getLocalProperties();
    }

    public static Optional<Pair<Properties, ClassPathResource>> getLocalProperties()
    {
        ClassPathResource resource = new ClassPathResource(LOCAL_PROPERTIES_FILE_NAME);
        return Try.withResources(resource::getInputStream).of(inputStream ->
        {
            Properties props = new Properties();
            props.load(inputStream);
            return Pair.of(props, resource);
        }).toJavaOptional();
    }
}
