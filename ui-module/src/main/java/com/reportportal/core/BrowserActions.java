package com.reportportal.core;

import java.util.List;
import java.util.Objects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.reportportal.browser.WebDriverHolder;
import com.reportportal.reporting.ReportService;

@Component
public class BrowserActions
{
    @Autowired
    private ReportService reportService;
    @Autowired
    private ElementHighlighter elementHighlighter;
    @Autowired
    private UiWait uiWait;
    @Value("${ui.wait.timeout.seconds}")
    private Long defaultTimeout;
    @Value("${ui.polling.timeout.milliseconds}")
    private Long pollingTimeOutMilliSeconds;

    public WebElement getWebElement(By by)
    {
        WebElement element = uiWait.until(wd -> wd.findElement(by), "Find element by locator: **%s**", by);
        uiWait.until(wd ->
        {
            new Actions(wd).moveToElement(element).perform();
            return element;
        }, "Move to element by locator: **%s**", by);
        elementHighlighter.highlight(element);
        return element;
    }

    public List<WebElement> getWebElements(By by)
    {
        return uiWait.until(wd -> wd.findElements(by), "Find list of elements by locator: **%s**", by);
    }

    public BrowserActions click(By by)
    {
        uiWait.until(wd ->
        {
            WebElement element = wd.findElement(by);
            element.click();
            elementHighlighter.highlight(element);
            return element;
        }, "Click on the element by locator: **%s**", by);
        return this;
    }

    public BrowserActions jsClick(By by)
    {
        reportService.debug("Perform JS click on the element by locator: **%s**", by);
        String clickScript = "arguments[0].click()";
        WebDriverHolder.getInstance().getJsExecutor().executeScript(clickScript, getWebElement(by));
        return this;
    }

    public String getText(WebElement element)
    {
        return Objects.nonNull(element.getText()) ? element.getText().trim() : null;
    }

    public String getAttribute(By by, String attributeKey)
    {
        WebElement element = getWebElement(by);
        String attributeValue = element.getAttribute(attributeKey);
        reportService.debug("Get attributeKey='%s' from the element by locator: **%s** => '%s'", attributeKey, by,
                attributeValue);
        return attributeValue;
    }

    public BrowserActions waitUntilPageLoad()
    {
        uiWait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
                .equals("complete"), "Wait until page load");
        return this;
    }

    public BrowserActions refreshPage()
    {
        WebDriverHolder.getInstance().getWebDriver().navigate().refresh();
        return this;
    }

}
