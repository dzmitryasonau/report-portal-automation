package com.reportportal.ui.pages;

import com.reportportal.annotations.Loggable;
import com.reportportal.annotations.Page;
import com.reportportal.annotations.Path;
import com.reportportal.ui.components.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.springframework.beans.factory.annotation.Autowired;

@Page
@Path("/ui/#{projectName}/launches/all")
public class LaunchesPage extends AbstractPage {
    private static final String OPEN_LAUNCH_TEMPLATE = "//div[@data-id='%s']//td[contains(@class,"
            + "'launchSuiteGrid__name')]//div[contains(@class,'itemInfo__main')]/a[contains(@class,"
            + "'itemInfo__name-link')]";
    private static final String OPEN_LAUNCH_GRID_TEMPLATE = "//div[contains(@class,'launchSuiteGrid')]/div" +
            "/div/a[contains(@class,'itemInfo__name-link') and contains(@href,'%s')]";

    private final By username = By.xpath("//div[contains(@class,'userBlock__username')]");
    private final By openUserBlock = By.xpath("//div[contains(@class,'userBlock__user-block')]");
    @Autowired
    private LaunchPage launchPage;

    @Loggable("User gets username")
    public String getUserName() {
        return browserActions.getText(username);
    }

    @Loggable("User opens userblock")
    public LaunchesPage openUserBlock() {
        browserActions.jsClick(openUserBlock);
        return this;
    }


    @Loggable("User opens launch")
    public LaunchPage openLaunch(String launchID) {
        try {
            browserActions.click(By.xpath(String.format(OPEN_LAUNCH_TEMPLATE, launchID)));
        } catch (ElementNotInteractableException e) {
            browserActions.jsClick(By.xpath(String.format(OPEN_LAUNCH_GRID_TEMPLATE, launchID)));
        }
        return launchPage;
    }
}
