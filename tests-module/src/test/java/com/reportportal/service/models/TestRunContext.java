package com.reportportal.service.models;

import com.codepine.api.testrail.TestRail;
import com.codepine.api.testrail.model.Run;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.testng.ITestContext;

import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class TestRunContext {
    private final TestRail testRail;
    private final Run testRun;
    private final ITestContext context;
    private final Map<Integer, Integer> resultsToCaseMapping;
}
