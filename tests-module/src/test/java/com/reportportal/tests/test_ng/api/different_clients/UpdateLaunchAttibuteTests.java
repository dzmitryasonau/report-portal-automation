package com.reportportal.tests.test_ng.api.different_clients;

import java.util.List;

import com.reportportal.api.HttpClient;
import com.reportportal.api.core.CustomResponse;
import com.reportportal.core.test_ng.AbstractTestNG;
import com.reportportal.models.User;
import com.reportportal.models.launch.api.Attribute;
import com.reportportal.service.AesCryptoService;
import com.reportportal.service.TestNGDataProvider;
import com.reportportal.service.UserDataService;
import com.reportportal.utils.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UpdateLaunchAttibuteTests extends AbstractTestNG
{
    private static final Integer LAST_LAUNCH_ID = 6262805;
    private static final String ATTRIBUTE_PREFIX = "Atr: ";
    private static final String ATTRIBUTE_VALUE = "value";
    private static final Integer MAX_INDEX = 10000;
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private AesCryptoService aesCryptoService;
    @Value("${rp.project}")
    private String projectName;
    @Autowired
    private HttpClient httpClient;
    private User user;
    private String token;

    @BeforeMethod
    private void setup()
    {
        user = userDataService.getUser();
        token = aesCryptoService.getDecryptedBearerApiKey(user);
    }

    @Test(dataProvider = "attributes", dataProviderClass = TestNGDataProvider.class)
    public void checkAttributeUpdates(Integer launchID, List<Attribute> attributes)
    {
        CustomResponse response = httpClient.updateLaunchAttributes(token, projectName, launchID, attributes);
        String expectedResponse = String.format("{\"message\":\"Launch with ID = '%s' successfully updated.\"}",
                launchID);
        verifyThat.actualIsEqualToExpected(response.getBody(), expectedResponse, "Response is correct, ");
    }

    @Test
    public void checkAttributeUpdateInvalidLaunchID()
    {
        CustomResponse response = httpClient.updateLaunchAttributes(token, projectName, -1,
                List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX), ATTRIBUTE_VALUE)));

        verifyThat.actualIsEqualToExpected(response.getStatusCode(), 404, "Status code is correct, ");
    }

    @Test
    public void checkAttributeUpdateInvalidProject()
    {
        projectName = "Invalid project name";
        CustomResponse response = httpClient.updateLaunchAttributes(token, projectName, LAST_LAUNCH_ID,
                List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX), ATTRIBUTE_VALUE)));

        verifyThat.actualIsEqualToExpected(response.getStatusCode(), 403, "Status code is correct, ");
    }

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }
}
