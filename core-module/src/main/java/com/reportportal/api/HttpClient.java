package com.reportportal.api;

import java.util.List;

import com.reportportal.api.core.CustomResponse;
import com.reportportal.models.launch.api.AnalysisRequest;
import com.reportportal.models.launch.api.Attribute;
import com.reportportal.models.launch.api.FinishLaunchRequest;
import com.reportportal.models.launch.api.LaunchMergeRequest;
import com.reportportal.models.launch.api.StartLaunchRequest;

public interface HttpClient
{
    CustomResponse getLaunchesByProjectName(String authToken, String projectName, String launchName);
    CustomResponse getLaunchByName(String authToken, String projectName, String launchName);
    CustomResponse updateLaunchAttributes(String authToken, String projectName, Integer launchID,
            List<Attribute> attributes);
    CustomResponse postNewLaunch(String authToken, String projectName, StartLaunchRequest startLaunchRequest);
    CustomResponse finishLaunch(String authToken, String projectName, String launchId, FinishLaunchRequest request);
    CustomResponse deleteLaunch(String authToken, String projectName, String launchId);
    CustomResponse analyzeLaunch(String authToken, String projectName, AnalysisRequest request);
    CustomResponse compareLaunch(String authToken, String projectName, List<Integer> launchesID);
    CustomResponse mergeLaunches(String authToken, String projectName, LaunchMergeRequest request);
}
