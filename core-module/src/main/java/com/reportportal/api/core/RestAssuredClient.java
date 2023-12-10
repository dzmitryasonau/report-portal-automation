package com.reportportal.api.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.reportportal.exceptions.AutomationException;
import com.reportportal.meta.ApiMethod;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestAssuredClient extends AbstractApiClient
{
    public CustomResponse sendRequest(String authToken, ApiMethod method, String requestUrl)
    {
        return sendRequest(authToken, method, requestUrl, null);
    }

    public CustomResponse sendRequest(String authToken, ApiMethod method, String requestUrl, Object requestBody)
    {
        RequestSpecification request = RestAssured.given();

        request.header("Authorization", authToken);
        request.header("Accept", "*/*");

        Optional.ofNullable(requestBody).ifPresent(b ->
        {
            request.header("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            request.body(b);
        });

        Response response = getRequestBase(request, method, requestUrl);

        Map<String, String> headers = new HashMap<>();
        response.getHeaders().asList().forEach(header -> headers.put(header.getName(), header.getValue()));

        return new CustomResponse(response.getStatusCode(), response.getBody().asString(), headers);
    }

    protected Response getRequestBase(RequestSpecification request, ApiMethod method, String requestUrl)
    {
        switch (method)
        {
            case GET ->
            {
                return request.get(requestUrl);
            }
            case POST ->
            {
                return request.post(requestUrl);
            }
            case PUT ->
            {
                return request.put(requestUrl);
            }
            case PATCH ->
            {
                return request.patch(requestUrl);
            }
            case DELETE ->
            {
                return request.delete(requestUrl);
            }
            default -> throw new AutomationException("Unable to process method: ", method);
        }
    }
}
