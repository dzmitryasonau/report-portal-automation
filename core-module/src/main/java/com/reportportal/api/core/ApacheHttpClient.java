package com.reportportal.api.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reportportal.exceptions.AutomationException;
import com.reportportal.meta.ApiMethod;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

public class ApacheHttpClient extends AbstractApiClient {

    public CustomResponse sendRequest(String authToken, ApiMethod method, String requestUrl) {
        return sendRequest(authToken, method, requestUrl, null);
    }

    public CustomResponse sendRequest(String authToken, ApiMethod method, String requestUrl, Object requestBody) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpUriRequestBase request = getRequestBase(method, requestUrl, requestBody);
            request.setHeader(HttpHeaders.AUTHORIZATION, authToken);
            request.setHeader(HttpHeaders.ACCEPT, "*/*");

            HttpClientResponseHandler<CustomResponse> handler = response -> {
                int responseCode = response.getCode();
                String entity = EntityUtils.toString(response.getEntity());
                Map<String, String> headers = new HashMap<>();
                Arrays.stream(response.getHeaders()).forEach(header ->
                        headers.put(header.getName(), header.getValue()));

                return new CustomResponse(responseCode, entity, headers);
            };

            return client.execute(request, handler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected HttpUriRequestBase getRequestBase(ApiMethod method, String requestUrl, Object requestBody) {
        try {
            switch (method) {
                case GET -> {
                    return new HttpGet(requestUrl);
                }
                case POST -> {
                    HttpPost httpPost = new HttpPost(requestUrl);
                    if (requestBody != null) {
                        StringEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(requestBody),
                                ContentType.APPLICATION_JSON);
                        httpPost.setEntity(entity);
                    }
                    return httpPost;
                }
                case PUT -> {
                    HttpPut httpPut = new HttpPut(requestUrl);
                    if (requestBody != null) {
                        StringEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(requestBody),
                                ContentType.APPLICATION_JSON);
                        httpPut.setEntity(entity);
                    }
                    return httpPut;
                }
                case PATCH -> {
                    return new HttpPatch(requestUrl);
                }
                case DELETE -> {
                    return new HttpDelete(requestUrl);
                }
                default -> throw new AutomationException("Unable to process method: ", method);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize request body", e);
        }
    }
}
