package com.reportportal.models.launch.api;

import lombok.Data;

@Data
public class FinishLaunchRequest
{
    private String endTime;
    private String status;

    public FinishLaunchRequest(String endTime, String status)
    {
        this.endTime = endTime;
        this.status = status;
    }
}
