package com.reportportal.api;

import com.reportportal.api.core.CustomResponseEntityDecoder;
import com.reportportal.api.core.CustomSpringDecoder;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.PageJacksonModule;
import org.springframework.cloud.openfeign.support.SortJacksonModule;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;

import feign.Contract;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import feign.form.spring.converter.SpringManyMultipartFilesReader;

@Configuration
@ComponentScan("com.reportportal")
@EnableFeignClients
public class SpringApiConfig
{
    @Bean
    public ObjectFactory<HttpMessageConverters> objectFactory()
    {
        return () -> new HttpMessageConverters(new FormHttpMessageConverter(), new SourceHttpMessageConverter(),
                new ResourceHttpMessageConverter(), new SpringManyMultipartFilesReader(4096),
                new StringHttpMessageConverter());
    }

    @Bean
    public Decoder feignDecoder()
    {
        return new CustomResponseEntityDecoder(new CustomSpringDecoder(objectFactory()));
    }

    @Bean
    public Encoder feignEncoder()
    {
        return new SpringFormEncoder(new SpringEncoder(objectFactory()));
    }

    @Bean
    public Contract.BaseContract baseContract()
    {
        return new SpringMvcContract();
    }

    @Bean
    public com.fasterxml.jackson.databind.Module pageJacksonModule()
    {
        return new PageJacksonModule();
    }

    @Bean
    public com.fasterxml.jackson.databind.Module sortJacksonModule()
    {
        return new SortJacksonModule();
    }
}
