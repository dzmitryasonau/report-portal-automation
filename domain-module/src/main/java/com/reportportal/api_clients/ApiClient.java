package com.reportportal.api_clients;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

public interface ApiClient
{
    @GetMapping(value = "api/v1/{projectName}/launch/names?filter.cnt.name={launchName}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = "Accept=" + MediaType.ALL_VALUE)
    ResponseEntity<List<String>> getLaunchesByProjectName(@RequestHeader("Authorization") String authToken,
            @PathVariable("projectName") String projectName, @PathVariable("launchName") String launchName);

}
