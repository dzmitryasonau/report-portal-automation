package com.reportportal.models.launch.api;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FinishLaunchResponse
{
    private String id;
    private int number;
    private String link;
}
