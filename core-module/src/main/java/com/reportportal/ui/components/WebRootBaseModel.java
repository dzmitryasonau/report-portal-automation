package com.reportportal.ui.components;

import com.reportportal.meta.BaseEntity;
import lombok.AllArgsConstructor;
import org.openqa.selenium.WebElement;

@AllArgsConstructor
public class WebRootBaseModel extends BaseEntity {
    private WebElement root;
}
