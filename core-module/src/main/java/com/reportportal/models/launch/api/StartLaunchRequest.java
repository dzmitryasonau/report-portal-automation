package com.reportportal.models.launch.api;

import lombok.Data;

@Data
public class StartLaunchRequest
{
    private String name;
    private String startTime;

    public StartLaunchRequest(String name, String startTime)
    {
        this.name = name;
        this.startTime = startTime;
    }
}
