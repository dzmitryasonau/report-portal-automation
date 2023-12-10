package com.reportportal.models.launch.api;

import java.util.List;

import lombok.Data;

@Data
public class UpdateLaunchRequest
{
    public UpdateLaunchRequest(List<Attribute> attributes)
    {
        this.attributes = attributes;
    }

    private List<Attribute> attributes;
}
