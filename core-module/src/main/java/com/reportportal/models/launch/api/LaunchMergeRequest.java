package com.reportportal.models.launch.api;

import java.util.List;

import lombok.Data;

@Data
public class LaunchMergeRequest
{
    private List<Integer> launches;
    private String mergeType = "BASIC";
    private boolean extendSuitesDescription;
    private String name;

    public LaunchMergeRequest(List<Integer> launches, boolean extendSuitesDescription, String name)
    {
        this.launches = launches;
        this.extendSuitesDescription = extendSuitesDescription;
        this.name = name;
    }
}
