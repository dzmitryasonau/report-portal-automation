package com.reportportal.service.models;

import com.codepine.api.testrail.model.ResultField;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.testng.ITestNGMethod;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class TestMethodContext {
    private final int tmsId;
    private final ITestNGMethod relatedMethod;
    private final List<ResultField> customResultFields;
    private boolean withDetails;
}
