package com.reportportal.api.core;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomResponse
{
    private int statusCode;
    private String body;
    private Map<String, String> headers;

    public CustomResponse(int statusCode, String body, Map<String, String> headers)
    {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
    }
}
