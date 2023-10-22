package com.reportportal.components;

import java.util.Optional;

import com.reportportal.annotations.Page;
import com.reportportal.annotations.Path;
import com.reportportal.browser.WebDriverHolder;
import com.reportportal.core.BrowserActions;
import com.reportportal.exceptions.TestExecutionException;
import com.reportportal.meta.BaseEntity;
import com.reportportal.reporting.ReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;

@Page
public abstract class AbstractPage extends BaseEntity
{
    @Autowired
    protected ReportService reportService;
    @Autowired
    protected BrowserActions browserActions;
    @Value("${app.base.url}")
    private String defaultUrl;

    public String getHomeUrl()
    {
        return defaultUrl;
    }

    public void openCurrentPage(Object... params) {
        String targetUrl = UriComponentsBuilder.fromUriString(getDeclaredFullUrl()).build(params).toString();
        openUrl(targetUrl);
    }

    public void openUrl(String url)
    {
        reportService.info("User opens url %s...", url);
        WebDriverHolder.getInstance().getWebDriver().get(url);
    }

    public String getCurrentUrl()
    {
        return WebDriverHolder.getInstance().getWebDriver().getCurrentUrl();
    }

    public String getDeclaredPathUrl() {
        String initialClassName = this.getClass().getSimpleName();
        Class<?> clazz = this.getClass();

        Optional<Path> path = Optional.empty();
        while (clazz != null) {
            if (clazz.isAnnotationPresent(Path.class)) {
                path = Optional.of(clazz.getAnnotation(Path.class));
                break;
            }
            clazz = clazz.getSuperclass();
        }
        if (path.isEmpty()) {
            throw new TestExecutionException("Class '%s' or its parent must be annotated with '%s' annotation to perform a navigation.", initialClassName, Path.class.getSimpleName());
        }
        return path.get().value();
    }

    public String getDeclaredFullUrl() {
        String homeUrl = getHomeUrl();
        String declaredPathUrl = getDeclaredPathUrl();
        //remove extra
        if (homeUrl.endsWith("/") && declaredPathUrl.startsWith("/")) {
            declaredPathUrl = declaredPathUrl.substring(1);
        }
        return homeUrl + declaredPathUrl;
    }
}
