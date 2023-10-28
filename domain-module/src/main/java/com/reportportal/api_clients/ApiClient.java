package com.reportportal.api_clients;

import java.util.List;
import java.util.Map;

import com.reportportal.models.launch.Launch;
import com.reportportal.models.launch.UpdateLaunchRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public interface ApiClient
{
    @GetMapping(value = "api/v1/{projectName}/launch/names?filter.cnt.name={launchName}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = "Accept=" + MediaType.ALL_VALUE)
    ResponseEntity<List<String>> getLaunchesByProjectName(@RequestHeader("Authorization") String authToken,
            @PathVariable("projectName") String projectName, @PathVariable("launchName") String launchName);

    @GetMapping(value = "api/v1/{projectName}/launch/latest",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = "Accept=" + MediaType.ALL_VALUE)
    ResponseEntity<Launch> getLatestLaunchByProjectName(@RequestHeader("Authorization") String authToken,
            @PathVariable("projectName") String projectName);

    @GetMapping(value = "api/v1/{projectName}/launch/status",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = "Accept=" + MediaType.ALL_VALUE)
    ResponseEntity<Map<String, String>> getLaunchStatus(@RequestHeader("Authorization") String authToken,
            @PathVariable("projectName") String projectName, @RequestParam("ids") Integer... launchID);

    @PutMapping(value = "api/v1/{projectName}/launch/{launchId}/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = "Accept=" + MediaType.ALL_VALUE)
    ResponseEntity<String> updateLaunch(@RequestHeader("Authorization") String authToken,
            @PathVariable("projectName") String projectName, @PathVariable("launchId") Integer launchId,
            @RequestBody UpdateLaunchRequest attributes);
}
