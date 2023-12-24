package com.reportportal.ui.core;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.reportportal.reporting.ReportService;
import com.reportportal.utils.CommonUtils;
import com.reportportal.utils.ObjectFormatUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SelenideActions
{
    private static final Integer MAX_SCROLLS = 10;
    private static final Integer MAX_REFRESHES = 10;
    @Value("${ui.polling.timeout.milliseconds}")
    private Long pollingTimeOutMilliSeconds;
    @Autowired
    private ReportService reportService;
    @Autowired
    private SelenideWait wait;

    public SelenideElement refreshUntilVisible(SelenideElement element) {
        int refreshCount = 0;
        while (!element.isDisplayed() && refreshCount < MAX_REFRESHES) {
            Selenide.refresh();
            refreshCount++;
            CommonUtils.sleep(pollingTimeOutMilliSeconds);
        }
        return element;
    }
    public SelenideActions dragAndDrop(SelenideElement source, SelenideElement target)
    {
        source.shouldBe(Condition.visible);
        target.shouldBe(Condition.visible);
        reportService.debug("Drag element: **%s** to target element: **%s**", source, target);
        Selenide.actions().dragAndDrop(source, target).perform();
        return this;
    }

    public SelenideActions resizeElement(SelenideElement element, int xOffset, int yOffset)
    {
        element.shouldBe(Condition.visible);
        reportService.debug("Resize element: **%s** to x:**%d** and y:**%d**", element, xOffset, yOffset);
        Selenide.actions().clickAndHold(element).moveByOffset(xOffset, yOffset).release().perform();
        return this;
    }

    /*
        Unable to use here JavaScript method such as: window.scrollBy(0, document.body.scrollHeight),
        because scroll relates to element but not the window
     */
    public SelenideActions scrollToElement(SelenideElement element)
    {
        reportService.debug("Scroll to element by locator: **%s**", element);
        SelenideElement body = Selenide.$(By.tagName("body"));
        int scrolls = 0;
        do
        {
            body.sendKeys(Keys.PAGE_DOWN);
            scrolls++;
        }
        while (!element.is(Condition.visible) && scrolls < MAX_SCROLLS);
        return this;
    }

    public boolean isElementInViewport(SelenideElement element)
    {
        reportService.debug("Check that element in viewport by locator: **%s**", element);
        Point location = element.toWebElement().getLocation();
        int elementPosition = location.getY();
        Selenide.executeJavaScript("window.scrollTo(0, arguments[0]);", elementPosition);
        int viewportHeight = Integer.parseInt(
                Objects.requireNonNull(Selenide.executeJavaScript("return window.innerHeight")).toString());
        int scrollTop = Integer.parseInt(
                Objects.requireNonNull(Selenide.executeJavaScript("return window.pageYOffset")).toString());

        return scrollTop <= elementPosition && (scrollTop + viewportHeight) >= elementPosition;

    }

    public SelenideActions jsClick(SelenideElement element)
    {
        reportService.debug("Perform JS click on the element by locator: **%s**", element);
        Selenide.executeJavaScript("arguments[0].click();", element);
        return this;
    }

    public SelenideActions click(SelenideElement element)
    {
        SelenideElement visibleElement = wait.until(webDriver ->
        {
            if (element.isDisplayed())
            {
                return element;
            }
            throw new NoSuchElementException("Element is not visible: " + element);
        }, "Waiting for element to be visible: **%s**", element);

        reportService.debug("Clicking on element: **%s**", visibleElement);
        visibleElement.click();
        return this;
    }

    public String getText(SelenideElement element)
    {
        SelenideElement visibleElement = wait.until(webDriver ->
        {
            if (element.isDisplayed())
            {
                return element;
            }
            throw new NoSuchElementException("Element is not visible: " + element);
        }, "Waiting for element to be visible: **%s**", element);

        String text = visibleElement.getText();
        reportService.debug("Retrieved text from element **%s**: **%s**", element, text);

        return text;
    }

    public SelenideActions setValue(SelenideElement element, String value)
    {
        SelenideElement visibleElement = wait.until(webDriver ->
        {
            if (element.isDisplayed())
            {
                return element;
            }
            throw new NoSuchElementException("Element is not visible: " + element);
        }, "Waiting for element to be visible: **%s**", element);

        reportService.debug("Setting value for element **%s**: **%s**", visibleElement, value);
        visibleElement.setValue(value);
        return this;
    }

    public SelenideActions setValue(SelenideElement element, String value, boolean isHideText)
    {
        if (Objects.isNull(value))
        {
            reportService.debug("No text is typed into the element **%s**", element);
            return this;
        }
        SelenideElement visibleElement = wait.until(webDriver ->
        {
            if (element.isDisplayed())
            {
                return element;
            }
            throw new NoSuchElementException("Element is not visible: " + element);
        }, "Waiting for element to be visible: **%s**", element);
        visibleElement.setValue(value);
        reportService.debug("Setting value for element **%s**: **%s**", visibleElement,
                ObjectFormatUtils.replaceWithStar(value));

        return this;
    }

    public List<String> getTexts(ElementsCollection elements)
    {
        List<String> texts = new ArrayList<>();
        for (SelenideElement element : elements)
        {
            SelenideElement visibleElement = wait.until(webDriver ->
            {
                scrollToElement(element);
                if (element.isDisplayed())
                {
                    return element;
                }
                throw new NoSuchElementException("Element is not visible: " + element);
            }, "Waiting for element to be visible: **%s**", element);

            reportService.debug("Retrieving text from element: **%s**", visibleElement);
            String text = visibleElement.getText();
            reportService.debug("Retrieved text: **%s**", text);
            texts.add(text);
        }
        return texts;
    }

    public SelenideActions waitUntilVisible(SelenideElement element)
    {
        wait.until(webDriver ->
        {
            if (element.isDisplayed())
            {
                return true;
            }
            throw new NoSuchElementException("Element is not visible: " + element);
        }, "Waiting for element to be visible: **%s**", element);
        reportService.debug("Element is visible: **%s**", element);
        return this;
    }
}
