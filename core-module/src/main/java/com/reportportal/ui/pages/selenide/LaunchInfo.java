package com.reportportal.ui.pages.selenide;

import java.util.List;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.reportportal.ui.components.AbstractSelenidePage;
import com.reportportal.ui.core.SelenideActions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LaunchInfo extends AbstractSelenidePage
{

    private static final String TEST_ROWS_TEMPLATE = "//div[contains(@class, 'grid-row-wrapper')]";
    private final ElementsCollection testRows = Selenide.$$x(TEST_ROWS_TEMPLATE);
    private final ElementsCollection testStatuses = Selenide.$$x(
            TEST_ROWS_TEMPLATE + "//span[contains(@class,'statusDropdown') and not(contains(@class,'arrow'))]");
    private final ElementsCollection defectTypes = Selenide.$$x("//div[contains(@class,'defect-type-name')]");
    private final SelenideElement suiteName = Selenide.$x("//span[text()='Suite name']");
    @Autowired
    private SelenideActions selenideActions;

    public boolean isSuiteNamePresent()
    {
        return suiteName.isDisplayed();
    }

    public ElementsCollection getTestRows()
    {
        ElementsCollection elements = testRows;
        elements.shouldHave(CollectionCondition.sizeGreaterThan(0));
        return elements;
    }

    public List<String> getTestStatuses()
    {
        ElementsCollection elements = testStatuses;
        elements.shouldHave(CollectionCondition.sizeGreaterThan(0));
        return selenideActions.getTexts(elements);
    }

    public List<String> getDefectTypes()
    {
        ElementsCollection elements = defectTypes;
        elements.shouldHave(CollectionCondition.sizeGreaterThan(0));
        return selenideActions.getTexts(elements);
    }
}
