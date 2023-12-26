package com.reportportal.ui.pages;

import com.reportportal.annotations.Loggable;
import com.reportportal.annotations.Page;
import com.reportportal.annotations.Path;
import com.reportportal.ui.components.AbstractPage;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;

@Page
@Path("/ui/#{projectName}/launches/all/{launchId}")
public class LaunchPage extends AbstractPage {
    private final By suites = By.xpath("//td[contains(@class,'launchSuiteGrid')]//div[contains(@class,"
            + "'tooltip__tooltip-trigger')]/span");
    private final By editSuiteItem = By.xpath("//a[contains(@class,'itemInfo__name')]");
    private final By grid = By.xpath("//div[contains(@class,'grid__grid')]");

    @Loggable("User gets suites names")
    public List<String> getSuitesName() {
        browserActions.waitUntilElementBeVisible(grid);
        return browserActions.getWebElements(suites).stream().map(e -> e.getAttribute("innerText")).collect(Collectors.toList());
    }
}
