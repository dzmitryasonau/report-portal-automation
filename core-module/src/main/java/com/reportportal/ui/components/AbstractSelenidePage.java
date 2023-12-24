package com.reportportal.ui.components;

import com.reportportal.annotations.Page;
import com.reportportal.meta.BaseEntity;
import com.reportportal.ui.core.SelenideActions;

import org.springframework.beans.factory.annotation.Autowired;

@Page
public abstract class AbstractSelenidePage extends BaseEntity
{
    @Autowired
    protected SelenideActions selenideActions;
}
