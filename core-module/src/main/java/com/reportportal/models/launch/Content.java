package com.reportportal.models.launch;

import java.util.List;

import com.reportportal.models.launch.api.Attribute;

import lombok.Data;

@Data
public class Content
{
    private String owner;
    private String description;
    private Integer id;
    private String uuid;
    private String name;
    private Integer number;
    private String startTime;
    private String endTime;
    private String lastModified;
    private String status;
    private Statistics statistics;
    private List<Attribute> attributes;
    private String mode;
    private List<Object> analysing;
    private Double approximateDuration;
    private Boolean hasRetries;
    private Boolean rerun;
}
