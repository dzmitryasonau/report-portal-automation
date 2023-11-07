package com.reportportal.ui.core;

import com.reportportal.ui.browser.WebDriverHolder;
import com.reportportal.reporting.ReportService;

import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ElementHighlighter {
    private static final String BORDER_STYLE = "arguments[0].style.border='%s'";
    private static final ThreadLocal<WebElement> highlightedElement = new ThreadLocal<>();

    @Value("${highlighting.enabled:false}")
    private boolean isEnabled;
    @Autowired
    private ReportService reportService;

    public void highlight(WebElement element) {
        if (isEnabled) {
            reset();
            try {
                WebDriverHolder.getInstance().getJsExecutor()
                        .executeScript(String.format(BORDER_STYLE, "3px solid red"), element);
                highlightedElement.set(element);
            } catch (Exception e) {
                reportService.debug("Unable to highlight element: [%s]", element.toString());
            }
        }
    }

    private void reset() {
        WebElement element = highlightedElement.get();
        if (element != null) {
            try {
                WebDriverHolder.getInstance().getJsExecutor().executeScript(String.format(BORDER_STYLE, ""), element);
            } catch (Exception e) {
                reportService.debug("Unable to unhighlight element: [%s]", element.toString());
            }
        }
        highlightedElement.remove();
    }
}
