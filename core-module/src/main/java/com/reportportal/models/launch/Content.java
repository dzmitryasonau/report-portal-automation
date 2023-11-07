package com.reportportal.models.launch;

import java.util.List;

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
    private Long startTime;
    private Long endTime;
    private Long lastModified;
    private String status;
    private Statistics statistics;
    private List<Attribute> attributes;
    private String mode;
    private List<Object> analysing;
    private Double approximateDuration;
    private Boolean hasRetries;
    private Boolean rerun;
}
