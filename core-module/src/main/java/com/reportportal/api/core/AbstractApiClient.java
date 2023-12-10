package com.reportportal.api.core;

import java.util.List;
import java.util.stream.Collectors;

import com.reportportal.api.HttpClient;
import com.reportportal.meta.ApiMethod;
import com.reportportal.models.launch.api.AnalysisRequest;
import com.reportportal.models.launch.api.Attribute;
import com.reportportal.models.launch.api.FinishLaunchRequest;
import com.reportportal.models.launch.api.LaunchMergeRequest;
import com.reportportal.models.launch.api.StartLaunchRequest;
import com.reportportal.models.launch.api.UpdateLaunchRequest;

import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractApiClient implements HttpClient
{
    @Value("${app.base.url}")
    private String url;

    @Override
    public CustomResponse getLaunchesByProjectName(String authToken, String projectName, String launchName)
    {
        String requestUrl = url + "api/v1/" + projectName + "/launch/names?filter.cnt.name=" + launchName;
        return sendRequest(authToken, ApiMethod.GET, requestUrl);
    }

    @Override
    public CustomResponse getLaunchByName(String authToken, String projectName, String launchName)
    {
        String requestUrl = url + "api/v1/" + projectName + "/launch?filter.eq.name=" + launchName;
        return sendRequest(authToken, ApiMethod.GET, requestUrl);
    }

    @Override
    public CustomResponse postNewLaunch(String authToken, String projectName, StartLaunchRequest startLaunchRequest)
    {
        String requestUrl = url + "api/v1/" + projectName + "/launch";
        return sendRequest(authToken, ApiMethod.POST, requestUrl, startLaunchRequest);
    }

    @Override
    public CustomResponse updateLaunchAttributes(String authToken, String projectName, Integer launchId,
            List<Attribute> attributes)
    {
        String requestUrl = url + "api/v1/" + projectName + "/launch/" + launchId + "/update";
        return sendRequest(authToken, ApiMethod.PUT, requestUrl, new UpdateLaunchRequest(attributes));
    }

    @Override
    public CustomResponse finishLaunch(String authToken, String projectName, String launchId,
            FinishLaunchRequest request)
    {
        String requestUrl = url + "api/v1/" + projectName + "/launch/" + launchId + "/finish";
        return sendRequest(authToken, ApiMethod.PUT, requestUrl, request);
    }

    @Override
    public CustomResponse deleteLaunch(String authToken, String projectName, String launchId)
    {
        String requestUrl = url + "api/v1/" + projectName + "/launch/" + launchId;
        return sendRequest(authToken, ApiMethod.DELETE, requestUrl);
    }

    @Override
    public CustomResponse analyzeLaunch(String authToken, String projectName, AnalysisRequest request)
    {
        String requestUrl = url + "api/v1/" + projectName + "/launch/analyze";
        return sendRequest(authToken, ApiMethod.POST, requestUrl, request);
    }

    @Override
    public CustomResponse mergeLaunches(String authToken, String projectName, LaunchMergeRequest request)
    {
        String requestUrl = url + "api/v1/" + projectName + "/launch/merge";
        return sendRequest(authToken, ApiMethod.POST, requestUrl, request);
    }

    @Override
    public CustomResponse compareLaunch(String authToken, String projectName, List<Integer> launchesID)
    {
        String requestUrl =
                url + "api/v1/" + projectName + "/launch/compare?" + launchesID.stream().map(launch -> "ids=" + launch)
                        .collect(Collectors.joining("&"));
        return sendRequest(authToken, ApiMethod.GET, requestUrl, launchesID);
    }

    protected abstract CustomResponse sendRequest(String authToken, ApiMethod method, String requestUrl);

    protected abstract CustomResponse sendRequest(String authToken, ApiMethod method, String requestUrl,
            Object requestBody);

}
