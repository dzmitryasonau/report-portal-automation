package com.reportportal.api.core;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.HttpMessageConverterExtractor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomSpringDecoder implements Decoder {

    private final ObjectFactory<HttpMessageConverters> messageConverters;

    private final ObjectProvider<HttpMessageConverterCustomizer> customizers;

    public CustomSpringDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        this(messageConverters, new EmptyObjectProvider<>());
    }

    private CustomSpringDecoder(ObjectFactory<HttpMessageConverters> messageConverters,
                                ObjectProvider<HttpMessageConverterCustomizer> customizers) {
        this.messageConverters = messageConverters;
        this.customizers = customizers;
    }

    @Override
    public Object decode(final Response response, Type type) throws IOException, FeignException {
        if (type instanceof Class || type instanceof ParameterizedType || type instanceof WildcardType) {
            List<HttpMessageConverter<?>> converters = messageConverters.getObject().getConverters();
            customizers.forEach(customizer -> customizer.accept(converters));
            @SuppressWarnings({"unchecked", "rawtypes"})
            HttpMessageConverterExtractor<?> extractor = new HttpMessageConverterExtractor(type, converters);

            return extractor.extractData(new FeignResponseAdapter(response));
        }
        throw new DecodeException(response.status(), "type is not an instance of Class or ParameterizedType: " + type,
                response.request());
    }

    private final class FeignResponseAdapter implements ClientHttpResponse {

        private final Response response;

        private FeignResponseAdapter(Response response) {
            this.response = response;
        }

        static HttpHeaders getHttpHeaders(Map<String, Collection<String>> headers) {
            HttpHeaders httpHeaders = new HttpHeaders();
            for (Map.Entry<String, Collection<String>> entry : headers.entrySet()) {
                httpHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
            return httpHeaders;
        }

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.valueOf(response.status());
        }

        @Override
        public String getStatusText() {
            return response.reason();
        }

        @Override
        public void close() {
            try {
                response.body().close();
            } catch (IOException ex) {
                // Ignore exception on close...
            }
        }

        @Override
        public InputStream getBody() throws IOException {
            return response.body().asInputStream();
        }

        @Override
        public HttpHeaders getHeaders() {
            return getHttpHeaders(response.headers());
        }

    }

}
