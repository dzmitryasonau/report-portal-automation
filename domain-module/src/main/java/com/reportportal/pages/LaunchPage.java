package com.reportportal.pages;

import java.util.List;
import java.util.stream.Collectors;

import com.reportportal.annotations.Loggable;
import com.reportportal.annotations.Page;
import com.reportportal.annotations.Path;
import com.reportportal.components.AbstractPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@Page
@Path("/ui/#{projectName}/launches/all/{launchId}")
public class LaunchPage extends AbstractPage
{
    private final By suites = By.xpath("//td[contains(@class,'launchSuiteGrid')]//div[contains(@class,"
            + "'tooltip__tooltip-trigger')]/span");
    private final By editSuiteItem = By.xpath("//td[contains(@class,'launchSuiteGrid')]//div/a");

    @Loggable("User gets suites names")
    public List<String> getSuitesName()
    {
        browserActions.waitUntilElementBeClickable(editSuiteItem);
        return browserActions.getWebElements(suites).stream().map(WebElement::getText).collect(Collectors.toList());
    }
}
