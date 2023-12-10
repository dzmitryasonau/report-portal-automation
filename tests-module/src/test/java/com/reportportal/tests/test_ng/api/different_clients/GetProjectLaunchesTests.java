package com.reportportal.tests.test_ng.api.different_clients;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reportportal.api.HttpClient;
import com.reportportal.api.core.CustomResponse;
import com.reportportal.core.test_ng.AbstractTestNG;
import com.reportportal.exceptions.AutomationException;
import com.reportportal.models.User;
import com.reportportal.service.AesCryptoService;
import com.reportportal.service.UserDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GetProjectLaunchesTests extends AbstractTestNG
{
    private static final String FILTER = "demo";
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private AesCryptoService aesCryptoService;
    @Value("${rp.project}")
    private String projectName;
    @Autowired
    private HttpClient httpClient;
    private User user;

    @BeforeMethod
    private void setup()
    {
        user = userDataService.getUser();
    }

    @Test
    public void checkGetProjectLaunchesWithFilter()
    {
        String token = aesCryptoService.getDecryptedBearerApiKey(user);
        CustomResponse response = httpClient.getLaunchesByProjectName(token, projectName, FILTER);
        try
        {
            List<String> actualLaunches = new ObjectMapper().readValue(response.getBody(), new TypeReference<>() {});
            verifyThat.itemIsPresentInList("Demo Api Tests", actualLaunches,
                    "Launch are presents in list, ");
        }
        catch (JsonProcessingException e)
        {
            throw new AutomationException("Error while converting response to List<String>", e);
        }
    }

    @Test
    public void checkGetProjectLaunchesWithFilterInvalidToken()
    {
        String token = "Invalid token";
        CustomResponse response = httpClient.getLaunchesByProjectName(token, projectName, FILTER);
        verifyThat.actualIsEqualToExpected(response.getStatusCode(), 401, "Status code is correct, ");
    }

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }
}
