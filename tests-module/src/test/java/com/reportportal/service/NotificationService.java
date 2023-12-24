package com.reportportal.service;

import com.codepine.api.testrail.TestRail;
import com.codepine.api.testrail.model.Case;
import com.codepine.api.testrail.model.Result;
import com.codepine.api.testrail.model.ResultField;
import com.codepine.api.testrail.model.Run;
import com.jameswarlick.teams.TeamsClient;
import com.jameswarlick.teams.api.Card;
import com.jameswarlick.teams.api.Section;
import com.reportportal.annotations.AutomationIssue;
import com.reportportal.annotations.Defects;
import com.reportportal.annotations.TmsId;
import com.reportportal.config.SpringDomainConfig;
import com.reportportal.core.test_ng.CustomTestNGService;
import com.reportportal.exceptions.AutomationException;
import com.reportportal.reporting.ReportService;
import com.reportportal.reporting.TestRailService;
import com.reportportal.support.PropertyHandlerHolder;
import io.vavr.control.Try;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class NotificationService {
    private final ReportService reportService;
    private final TestRailService testRailService;
    private final boolean notificationsEnabled;
    private final boolean teamsEnabled;
    private final String teamsWebHook;
    private static final Map<Integer, Integer> TEST_NG_TO_TEST_RAIL_STATUS_MAPPING = new ConcurrentHashMap<>();

    static {
        TEST_NG_TO_TEST_RAIL_STATUS_MAPPING.put(ITestResult.CREATED, TestRailResultStatus.INVALID.value);
        TEST_NG_TO_TEST_RAIL_STATUS_MAPPING.put(ITestResult.SUCCESS, TestRailResultStatus.PASSED.value);
        TEST_NG_TO_TEST_RAIL_STATUS_MAPPING.put(ITestResult.FAILURE, TestRailResultStatus.FAILED.value);
        TEST_NG_TO_TEST_RAIL_STATUS_MAPPING.put(ITestResult.SKIP, TestRailResultStatus.SKIPPED.value);
        TEST_NG_TO_TEST_RAIL_STATUS_MAPPING.put(ITestResult.SUCCESS_PERCENTAGE_FAILURE, TestRailResultStatus.RETEST.value);
        TEST_NG_TO_TEST_RAIL_STATUS_MAPPING.put(ITestResult.STARTED, TestRailResultStatus.INVALID.value);
    }

    public NotificationService() {
        var propertySourcesPlaceholder = PropertyHandlerHolder.getInstance()
                .init(SpringDomainConfig.getPropertiesResources()).getPropertyConfigurer();
        testRailService = new TestRailService(propertySourcesPlaceholder);
        reportService = new ReportService();
        notificationsEnabled = Boolean.parseBoolean(propertySourcesPlaceholder.get("notifications.enabled"));
        teamsEnabled = Boolean.parseBoolean(propertySourcesPlaceholder.get("teams.enabled"));
        teamsWebHook = propertySourcesPlaceholder.get("teams.webhook");
    }

    public void onFinish(ITestContext context) {
        if (!notificationsEnabled) {
            return;
        }
        Try.runRunnable(() ->
        {
            if (testRailService.isEnabled()) {
                reportService.debug("TestRail reporting STARTED");
                postResultsToTheTestRail(context);
                reportService.debug("TestRail reporting FINISHED");
            }
        }).onFailure(s -> reportService.warn("Error during posting results to the TestRail: " + s.getMessage()));
    }

    private void postResultsToTheTestRail(ITestContext context) {
        TestRail testRail = TestRail.builder(testRailService.getUrl(), testRailService.getUserName(),
                testRailService.getApiKey()).build();

        Map<Integer, ITestNGMethod> testCaseData = getAllTestMethods(context).stream()
                .filter(r -> r.getConstructorOrMethod().getMethod().getAnnotation(TmsId.class) != null).collect(
                        Collectors.toMap(k -> k.getConstructorOrMethod().getMethod().getAnnotation(TmsId.class).value(),
                                v -> v, (v1, v2) -> v1));

        var allCases = testRail.cases().list(testRailService.getProjectId(), testRail.caseFields().list().execute())
                .execute();
        List<Integer> caseIds = getCases(allCases);
        String runName = "Report Portal test automation run";
        Run testSuite = new Run().setName(runName).setDescription("Auto Description")
                .setIncludeAll(false).setCaseIds(caseIds);
        Run testRun = testRail.runs().add(testRailService.getProjectId(), testSuite).execute();
        List<ResultField> customResultFields = testRail.resultFields().list().execute();

        Map<Integer, Integer> resultsToCaseMapping = new HashMap<>();

        testCaseData.forEach((tmsId, testNgMethod) ->
        {
            boolean isSuccessfully = postResultOperationSuccessfully(() -> postResult(testRail, testRun, tmsId,
                    testNgMethod, context, customResultFields, true, resultsToCaseMapping), tmsId, testNgMethod);
            if (!isSuccessfully) {
                postResultOperationSuccessfully(() ->
                {
                    postResult(testRail, testRun, tmsId, testNgMethod, context, customResultFields, false,
                            resultsToCaseMapping);
                }, tmsId, testNgMethod);
            }
        });
        if (testRailService.isCompleteRun()) {
            testRail.runs().close(testRun.getId()).execute();
        }


        if (teamsEnabled) {
            postResultsToTeams(testRail, runName, testRailService.getUrl(), testRun.getId(), resultsToCaseMapping);
        }
    }

    private void postResultsToTeams(TestRail testRail, String runName, String testRailUrl, int runId, Map<Integer, Integer> resultsToCaseMapping) {
        String runUrl = String.format("%sindex.php?/runs/view/%s", testRailUrl, runId);
        List<Result> casesResults = testRail.results().listForRun(runId, testRail.resultFields().list().execute()).execute();
        String cardMessage = getTeamsResultsMessage(casesResults, runUrl, testRailUrl, resultsToCaseMapping);
        Card card = getTeamsResultCard(runName, cardMessage);
        new TeamsClient(teamsWebHook).sendMessage(card);
    }

    private Card getTeamsResultCard(String runName, String cardMessage) {
        Card card = new Card("Card \"Test card\"", "0078D7", "Launch results: ");
        List<Section> sections = new ArrayList<>();
        Section sectionOne = new Section(
                runName,
                LocalDateTime.now().toString(),
                "https://cdn.pixabay.com/photo/2014/06/03/19/38/road-sign-361513_1280.jpg");//hardcoded image
        sectionOne.setText(cardMessage);
        sections.add(sectionOne);
        card.setSections(sections);
        return card;
    }

    private String getTeamsResultsMessage(List<Result> casesResults, String runUrl, String testRailUrl, Map<Integer, Integer> resultsToCaseMapping) {
        long testRailTotal = casesResults.size();
        long testRailPassed = casesResults.stream().filter(f -> TestRailResultStatus.PASSED.value == f.getStatusId()).count();
        StringBuilder sb = new StringBuilder();
        sb.append("Overall result ");
        if (testRailTotal == 0L) {
            sb.append(" - NO TEST RESULTS DATA");
            return sb.toString();
        }

        if(testRailTotal != testRailPassed){
            sb.append(" 🔴");
        } else {
            sb.append(" ✅");
        }


        Try.runRunnable(() -> {
            var rpData = CustomTestNGService.getRpLaunchUiData();
            var launchData = rpData.first();
            var launchUiUrl = rpData.second();
            sb.append(String.format("%n%n[%s](%s): ", "Report Portal Details " + launchData.getName(), launchUiUrl));
            var executions = launchData.getStatisticsResource().getExecutions();

            int rpPassed = executions.getOrDefault("passed", 1);
            int rpFailed = executions.getOrDefault("failed", 0);
            int rpTotal = executions.getOrDefault("total", 0);
            double rpPassRate = 100.0 * (double) rpPassed / (double) rpTotal;
            long endTime = Objects.isNull(launchData.getEndTime()) ? new Date().getTime() : launchData.getEndTime().getTime();
            long durationMs = endTime - launchData.getStartTime().getTime();
            long durationMinutes = (durationMs / 1000) / 60;
            long durationSeconds = (durationMs / 1000) % 60;

            sb.append(String.format("Total - %s, Failed - %s, *PASS RATE* - %s", rpTotal, rpFailed,
                    new DecimalFormat("0.00").format(rpPassRate)));
            sb.append("%");
            sb.append(String.format("%n%nDuration: %d min %d sec", durationMinutes, durationSeconds));
        });

        sb.append(String.format("%n%n[%s](%s): ", "TestRail Run Details", runUrl));
        if (testRailTotal != testRailPassed) {
            sb.append(String.format("%n%nFailed tests:%n%n"));
            casesResults.stream()
                    .filter(f -> TestRailResultStatus.PASSED.value != f.getStatusId())
                    .sorted(Comparator.comparingInt(Result::getStatusId))
                    .forEach(res -> {
                        String itemRunUrl = String.format("%sindex.php?/tests/view/%s", testRailUrl, res.getTestId());
                        var tmsId = resultsToCaseMapping.containsKey(res.getId()) ? resultsToCaseMapping.get(res.getId()) : res.getTestId();
                        String line = String.format("• [%s](%s)%n%n", "C" + tmsId, itemRunUrl);
                        sb.append(line);
                    });
        }
        return sb.toString();
    }

    private List<Integer> getCases(List<Case> cases) {
        return cases.stream().map(Case::getId).toList();
    }

    private boolean postResultOperationSuccessfully(Runnable run, int tmsId, ITestNGMethod testNgMethod) {
        try {
            run.run();
            return true;
        } catch (Exception e) {
            reportService.warn(
                    "TestRail: error happened during posting test result for the tmsId='%s', methodName='%s'. "
                            + "Details:'%s'",
                    tmsId, testNgMethod.getMethodName(), e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("checkstyle:parameterNumber")
    private void postResult(TestRail testRail, Run testRun, int tmsId, ITestNGMethod relatedMethod,
                            ITestContext context, List<ResultField> customResultFields, boolean withDetails,
                            Map<Integer, Integer> resultsToCaseMapping) {
        List<ITestResult> allTestResults = getAllResults(relatedMethod, context);

        List<ITestResult> failed = allTestResults.stream().filter(r -> ITestResult.FAILURE == r.getStatus())
                .collect(Collectors.toList());

        List<ITestResult> skipped = allTestResults.stream().filter(r -> ITestResult.SKIP == r.getStatus())
                .collect(Collectors.toList());

        Optional<Defects> optionalDefects = TestNgRpUtils.getOrEmptyAnnotation(relatedMethod, Defects.class);
        Optional<AutomationIssue> optionalAutomationIssues = TestNgRpUtils.getOrEmptyAnnotation(relatedMethod,
                AutomationIssue.class);
        int statusId;

        //considered test as failed
        if (!failed.isEmpty()) {
            statusId = TestNgRpUtils.isKnownIssue(optionalDefects, failed)
                    ? TestRailResultStatus.KNOWN_ISSUE.value
                    : testNgStatusToTestRailStatus(ITestResult.FAILURE);
            if (statusId == testNgStatusToTestRailStatus(ITestResult.FAILURE)) {
                statusId = TestNgRpUtils.isAutomationIssue(optionalAutomationIssues, failed)
                        ? TestRailResultStatus.AUTOMATION_ISSUE.value
                        : testNgStatusToTestRailStatus(ITestResult.FAILURE);
            }
        } else if (!skipped.isEmpty()) {
            statusId = TestNgRpUtils.isKnownIssue(optionalDefects, skipped)
                    ? TestRailResultStatus.KNOWN_ISSUE.value
                    : testNgStatusToTestRailStatus(ITestResult.SKIP);
            if (statusId == testNgStatusToTestRailStatus(ITestResult.SKIP)) {
                statusId = TestNgRpUtils.isAutomationIssue(optionalAutomationIssues, skipped)
                        ? TestRailResultStatus.AUTOMATION_ISSUE.value
                        : testNgStatusToTestRailStatus(ITestResult.SKIP);
            }
        } else {
            statusId = testNgStatusToTestRailStatus(ITestResult.SUCCESS);
        }

        String comment = getComment(allTestResults, withDetails);
        List<String> defects = TestNgRpUtils.getDefects(optionalDefects);
        defects.addAll(TestNgRpUtils.getAutomationIssues(optionalAutomationIssues));
        Result testRailResult = new Result().setDefects(TestNgRpUtils.getDefects(optionalDefects)).setComment(comment);

        //if not Untested
        if (TestRailResultStatus.UNTESTED.value != statusId) {
            testRailResult.setStatusId(statusId);
        }

        var results = testRail.results().addForCase(testRun.getId(), tmsId, testRailResult, customResultFields)
                .execute();

        resultsToCaseMapping.put(results.getId(), tmsId);
    }

    private List<ITestResult> getAllResults(ITestNGMethod relatedMethod, ITestContext context) {
        List<ITestResult> allTestResults = new CopyOnWriteArrayList<>(
                context.getPassedTests().getResults(relatedMethod));
        allTestResults.addAll(context.getFailedTests().getResults(relatedMethod));
        allTestResults.addAll(context.getFailedButWithinSuccessPercentageTests().getResults(relatedMethod));
        allTestResults.addAll(context.getSkippedTests().getResults(relatedMethod));
        return allTestResults;
    }

    private List<ITestNGMethod> getAllTestMethods(ITestContext context) {
        List<ITestNGMethod> allTestResults = new CopyOnWriteArrayList<>(context.getPassedTests().getAllMethods());
        allTestResults.addAll(context.getFailedTests().getAllMethods());
        allTestResults.addAll(context.getFailedButWithinSuccessPercentageTests().getAllMethods());
        allTestResults.addAll(context.getSkippedTests().getAllMethods());
        return allTestResults;
    }

    private String getComment(List<ITestResult> allResults, boolean withDetails) {
        return allResults.stream().map(r ->
        {
            String statusString;
            if (ITestResult.FAILURE == r.getStatus()) {
                statusString = "FAILED";
            } else if (ITestResult.SKIP == r.getStatus()) {
                statusString = "SKIPPED";
            } else {
                statusString = "PASSED";
            }
            String details = "";
            if (withDetails) {
                details = Objects.nonNull(r.getThrowable())
                        ? " Exception details: " + r.getThrowable().getMessage()
                        : "";
                details = details.replace("<", "").replace(">", "");
            }
            String remoteLink = TestNgRpUtils.getAttribute(r, CustomTestNGService.RP_REMOTE_LINK);
            remoteLink = Objects.nonNull(remoteLink) ? String.format(" SauceLabs results - %s", remoteLink) : "";
            String rpLink = TestNgRpUtils.getAttribute(r, CustomTestNGService.RP_LOG_ITEM_UI_LINK);
            rpLink = Objects.nonNull(rpLink) ? String.format(" RP results - %s", rpLink) : "";
            return r.getTestClass().getName() + "#" + r.getMethod().getMethodName() + " - " + statusString + rpLink
                    + remoteLink + details;
        }).collect(Collectors.joining(System.lineSeparator()));
    }

    private int testNgStatusToTestRailStatus(int testNgStatus) {
        if (!TEST_NG_TO_TEST_RAIL_STATUS_MAPPING.containsKey(testNgStatus)) {
            throw new RuntimeException(
                    String.format("No mapping of testNgStatus='%s' to TestRail status", testNgStatus));
        }
        return TEST_NG_TO_TEST_RAIL_STATUS_MAPPING.get(testNgStatus);
    }

    public enum TestRailResultStatus {
        PASSED(1), BLOCKED(2), UNTESTED(3), RETEST(4), FAILED(5), INVALID(8), SKIPPED(9), KNOWN_ISSUE(
                10), AUTOMATION_ISSUE(11);
        public final int value;

        TestRailResultStatus(int value) {
            this.value = value;
        }

        public static TestRailResultStatus fromInt(int value) {
            for (TestRailResultStatus b : TestRailResultStatus.values()) {
                if (b.value == value) {
                    return b;
                }
            }
            throw new AutomationException("Unable to find TestRailResultStatus by status %s", value);
        }
    }
}

