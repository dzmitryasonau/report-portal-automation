package com.reportportal.models.launch.api;

import java.util.List;

import lombok.Data;

@Data
public class AnalysisRequest
{
    private List<String> analyzeItemsMode;
    private String analyzerMode;
    private String analyzerTypeName;
    private int launchId;

    public AnalysisRequest(List<String> analyzeItemsMode, String analyzerMode, String analyzerTypeName, int launchId)
    {
        this.analyzeItemsMode = analyzeItemsMode;
        this.analyzerMode = analyzerMode;
        this.analyzerTypeName = analyzerTypeName;
        this.launchId = launchId;
    }
}
