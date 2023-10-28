package com.reportportal.models.launch;

import lombok.Data;

@Data
public class Attribute
{
    private String key;
    private String value;

    public Attribute(String key, String value)
    {
        this.key = key;
        this.value = value;
    }
}
