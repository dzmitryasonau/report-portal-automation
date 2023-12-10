package com.reportportal.config;

import java.util.ArrayList;
import java.util.List;

import com.reportportal.api.HttpClientConfig;
import com.reportportal.api.SpringApiConfig;
import com.reportportal.support.CustomPropertySourcesPlaceholderConfigurer;
import com.reportportal.support.PropertyHandlerHolder;
import com.reportportal.ui.SpringUiConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
@ComponentScan({ "com.reportportal" })
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({ SpringUiConfig.class, SpringApiConfig.class, HttpClientConfig.class, SpringCoreConfig.class })
public class SpringDomainConfig
{

    public static Resource[] getPropertiesResources()
    {
        ClassPathResource[] classPathResources = new ClassPathResource[] {
                new ClassPathResource("application.properties"), new ClassPathResource("common.properties") };
        var optionalLocalFile = ResourcesData.getLocalProperties();
        if (optionalLocalFile.isPresent())
        {
            var list = new ArrayList<>(List.of(classPathResources));
            list.add(optionalLocalFile.get().getSecond());
            classPathResources = list.toArray(ClassPathResource[]::new);
        }

        return classPathResources;
    }

    @Bean
    public CustomPropertySourcesPlaceholderConfigurer properties()
    {
        return PropertyHandlerHolder.getInstance().init(getPropertiesResources()).getPropertyConfigurer();
    }
}
