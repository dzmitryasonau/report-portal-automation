package com.reportportal.pages;

import com.reportportal.annotations.Loggable;
import com.reportportal.annotations.Page;
import com.reportportal.annotations.Path;
import com.reportportal.components.AbstractPage;

import org.openqa.selenium.By;

@Page
@Path("/ui/#{userName}_personal/launches/all")
public class LaunchesPage extends AbstractPage
{
    private final By username = By.xpath("//div[contains(@class,'userBlock__username')]");
    private final By openUserBlock = By.xpath("//div[contains(@class,'userBlock__user-block')]");

    @Loggable("User gets username")
    public String getUserName()
    {
        browserActions.jsClick(openUserBlock);
        return browserActions.getText(username);
    }
}
