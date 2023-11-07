package com.reportportal.api.core;

import com.reportportal.api.reporting.CustomFeignLogger;

import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.hc5.ApacheHttp5Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FeignBaseClient {
    @Autowired
    private CustomFeignLogger logger;
    @Autowired
    private Encoder encoder;
    @Autowired
    private Decoder decoder;
    @Autowired
    private Contract.BaseContract baseContract;

    @Bean
    public Feign.Builder getFeignBuilder() {
        return Feign.builder().contract(baseContract).encoder(encoder).decoder(decoder).logger(logger).logLevel(Logger.Level.BASIC).client(new ApacheHttp5Client());
    }
}
