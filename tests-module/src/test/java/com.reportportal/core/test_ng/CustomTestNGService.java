package com.reportportal.core.test_ng;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;

import com.epam.reportportal.annotations.attribute.Attributes;
import com.epam.reportportal.listeners.ItemStatus;
import com.epam.reportportal.service.Launch;
import com.epam.reportportal.service.tree.TestItemTree;
import com.epam.reportportal.testng.TestMethodType;
import com.epam.reportportal.testng.TestNGService;
import com.epam.reportportal.testng.util.ItemTreeUtils;
import com.epam.reportportal.utils.AttributeParser;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.attribute.ItemAttributesRQ;
import com.epam.ta.reportportal.ws.model.issue.Issue;
import com.epam.ta.reportportal.ws.model.launch.LaunchResource;
import com.epam.ta.reportportal.ws.model.launch.Mode;
import com.reportportal.browser.WebDriverHolder;
import com.reportportal.config.SpringDomainConfig;
import com.reportportal.core.utils.SauceLabsTextNGCloudParameters;
import com.reportportal.core.utils.TestNgRpUtils;
import com.reportportal.reporting.TestRailService;
import com.reportportal.services.SauceLabsService;
import com.reportportal.support.properties.PropertyHandlerHolder;
import com.reportportal.utils.GenericBuilder;

import org.openqa.selenium.remote.SessionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.internal.TestResult;
import org.testng.internal.collections.Pair;
import org.testng.xml.XmlClass;

import io.reactivex.Maybe;
import io.vavr.control.Try;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

public class CustomTestNGService extends TestNGService
{

    public static final String RP_LOG_ITEM_UI_LINK = "rp_log_item_ui_link";
    public static final String RP_REMOTE_LINK = "rp_remote_link";
    private static final String RP_CLASS_COUNT_KEY = "rp_class_count";
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomTestNGService.class);
    private static final Logger RP_BINARY_LOGGER = LoggerFactory.getLogger("binary_data_logger");
    private final TestRailService testRailService;
    private final SauceLabsService sauceLabsService;
    private final SauceLabsTextNGCloudParameters sauceLabsTextNGCloudParameters;

    public CustomTestNGService()
    {
        var propertySourcesPlaceholder = PropertyHandlerHolder.getInstance()
                .init(SpringDomainConfig.getPropertiesResources()).getPropertyConfigurer();
        testRailService = new TestRailService(propertySourcesPlaceholder);
        sauceLabsService = new SauceLabsService(propertySourcesPlaceholder);
        sauceLabsTextNGCloudParameters = new SauceLabsTextNGCloudParameters();
    }

    @Override
    public void startTest(ITestContext testContext)
    {
        //do nothing
    }

    @Override
    public void finishTest(ITestContext testContext)
    {
        if (hasMethodsToRun(testContext))
        {
            FinishTestItemRQ rq = buildFinishTestRq(testContext);
            Launch myLaunch = Launch.currentLaunch();

            Set<Maybe<String>> itemsSet = getAttribute(testContext, RP_CLASS_COUNT_KEY);
            itemsSet.forEach(item -> myLaunch.finishTestItem(item, rq));

            if (myLaunch.getParameters().isCallbackReportingEnabled())
            {
                removeFromTree(testContext);
            }
        }
    }

    @Override
    public void startTestMethod(@Nonnull ITestResult testResult)
    {
        Launch myLaunch = Launch.currentLaunch();
        Maybe<String> testID = manageTestItem(testResult);

        LOGGER.debug("<<< The test '{}' is started >>>", createStepName(testResult));
        StartTestItemRQ rq = buildStartStepRq(testResult);
        if (Boolean.TRUE == rq.isRetry())
        {
            testResult.setAttribute(RP_RETRY, Boolean.TRUE);
        }

        var attributes = rq.getAttributes();
        if (Objects.isNull(attributes))
        {
            attributes = new HashSet<>();
        }
        attributes.addAll(getAttributesFromClass(testResult.getMethod().getTestClass().getRealClass()));
        if (WebDriverHolder.getInstance().isRemoteRun())
        {
            attributes.add(new ItemAttributesRQ("SLID", WebDriverHolder.getInstance().getSessionId().toString()));
            attributes.add(new ItemAttributesRQ("SLDC", sauceLabsService.getDataCenterShort()));
            rq.setAttributes(attributes);
        }

        Maybe<String> stepMaybe = myLaunch.startTestItem(testID, rq);
        testResult.setAttribute(RP_ID, stepMaybe);
        //add ui link to the log item:
        Try.runRunnable(() ->
        {
            String id = stepMaybe.blockingGet();
            var testItemResource = myLaunch.getClient().getItemByUuid(id).blockingGet();
            String uiLink = getRpLogItemUiLink(myLaunch, testItemResource.getLaunchId(), testItemResource.getPath());
            testResult.setAttribute(RP_LOG_ITEM_UI_LINK, uiLink);
        });
        if (myLaunch.getParameters().isCallbackReportingEnabled())
        {
            addToTree(testResult, stepMaybe);
        }
    }

    private Maybe<String> manageTestItem(@Nonnull ITestResult testResult)
    {
        Launch myLaunch = Launch.currentLaunch();
        Maybe<String> testID;
        synchronized (CustomTestNGService.class)
        {
            String classKey = getClassKey(testResult);
            testID = getAttribute(testResult.getTestContext(), classKey);

            if (Objects.isNull(testID))
            {
                StartTestItemRQ rqTest = buildStartTestItemRq(testResult.getTestContext());
                var clazz = testResult.getMethod().getTestClass().getRealClass();
                String itemName = clazz.getSimpleName();
                rqTest.setName(itemName);
                rqTest.setAttributes(getAttributesFromClass(clazz));
                rqTest.setCodeRef(clazz.getName());

                testID = myLaunch.startTestItem(this.getAttribute(testResult.getTestContext().getSuite(), RP_ID),
                        rqTest);
                if (myLaunch.getParameters().isCallbackReportingEnabled())
                {
                    addToTree(testResult.getTestContext(), testID);
                }
                testResult.getTestContext().setAttribute(classKey, testID);

                Set<Maybe<String>> itemsSet = getAttribute(testResult.getTestContext(), RP_CLASS_COUNT_KEY);
                if (Objects.isNull(itemsSet))
                {
                    itemsSet = new CopyOnWriteArraySet<>();
                }
                itemsSet.add(testID);
                testResult.getTestContext().setAttribute(RP_CLASS_COUNT_KEY, itemsSet);
            }
        }
        return testID;
    }

    @Override
    public void finishTestMethod(ItemStatus status, ITestResult testResult)
    {
        Try.runRunnable(() ->
        {
            if (WebDriverHolder.getInstance().isRemoteRun())
            {
                var sessionId = WebDriverHolder.getInstance().getSessionId();
                String link = sauceLabsService.getPublicJobLink(sessionId).getOrElse("Unable to get public job link");
                String message = String.format("Remote Run info - %s", link);
                LOGGER.info(message);
                testResult.setAttribute(RP_REMOTE_LINK, link);
                sauceLabsService.updateJobInfo(sessionId,
                        sauceLabsTextNGCloudParameters.getCloudParameters(testResult));
            }
        });
        if (ITestResult.FAILURE == testResult.getStatus())
        {
            Try.run(() ->
            {
                //report screenshot
                String fileName = String.format("Screenshot_%s_%s", Thread.currentThread().getId(),
                        System.currentTimeMillis());
                Screenshot screenshot = new AShot().takeScreenshot(WebDriverHolder.getInstance().getWebDriver());
                var screenShootFile = File.createTempFile(fileName, ".png");
                ImageIO.write(screenshot.getImage(), "PNG", screenShootFile);
                logFile(screenShootFile, WebDriverHolder.getInstance().getWebDriver().getCurrentUrl());
            });
        }
        super.finishTestMethod(status, testResult);
    }

    @Override
    protected FinishTestItemRQ buildFinishTestMethodRq(@Nonnull ItemStatus status, @Nonnull ITestResult testResult)
    {
        FinishTestItemRQ rq = super.buildFinishTestMethodRq(status, testResult);
        if (ItemStatus.FAILED == status)
        {
            GenericBuilder<Issue> issue = GenericBuilder.of(Issue::new).with(s -> s.setAutoAnalyzed(true));
            rq.setIssue(issue.build());
        }
        return rq;
    }

    @Override
    public void startConfiguration(ITestResult testResult)
    {
        TestMethodType type = TestMethodType.getStepType(testResult.getMethod());
        testResult.setAttribute(RP_METHOD_TYPE, type);
        StartTestItemRQ rq = buildStartConfigurationRq(testResult, type);
        if (Boolean.TRUE == rq.isRetry())
        {
            testResult.setAttribute(RP_RETRY, Boolean.TRUE);
        }
        Maybe<String> parentId = manageTestItem(testResult);
        Launch myLaunch = Launch.currentLaunch();
        Maybe<String> itemID = myLaunch.startTestItem(parentId, rq);
        testResult.setAttribute(RP_ID, itemID);
    }

    @Override
    protected String createStepDescription(ITestResult testResult)
    {
        Method method = testResult.getMethod().getConstructorOrMethod().getMethod();
        Integer[] testCaseIds = TestNgRpUtils.getTestCaseIds(method);
        StringBuilder sb = new StringBuilder();

        appendData(sb, testCaseIds, "TestRail:", testRailService::getFullLinkToTheTestCaseId);

        if (WebDriverHolder.getInstance().isRemoteRun())
        {
            SessionId sessionId = WebDriverHolder.getInstance().getSessionId();

            sauceLabsService.getPublicJobLink(sessionId).onSuccess(link ->
            {
                sb.append("SauceLabsInfo:").append(System.lineSeparator());
                sb.append(link).append(System.lineSeparator());
            });
        }
        return sb.toString();
    }

    @Override
    protected String createStepName(ITestResult testResult)
    {
        String methodName = testResult.getMethod().getMethodName();
        methodName +=
                testResult.getParameters().length > 0 ? "[" + ((TestResult) testResult).getParameterIndex() + "]" : "";
        return methodName;
    }

    @Override
    protected StartTestItemRQ buildStartSuiteRq(ISuite suite)
    {
        StartTestItemRQ rq = super.buildStartSuiteRq(suite);
        rq.setName("report-portal");
        return rq;
    }

    public static Pair<LaunchResource, String> getRpLaunchUiData()
    {
        Launch launch = Launch.currentLaunch();
        var parameters = launch.getParameters();
        String url = parameters.getBaseUrl();
        String modeType = parameters.getLaunchRunningMode() == Mode.DEBUG ? "userdebug" : "launches";
        String project = parameters.getProjectName();
        LaunchResource launchData = launch.getClient().getLaunchByUuid(launch.getLaunch().blockingGet()).blockingGet();
        String launchUiUrl = String.format("%sui/#%s/%s/all/%s", url, project, modeType, launchData.getLaunchId());
        return new Pair<>(launchData, launchUiUrl);
    }

    private <T> void appendData(StringBuilder sb, T[] array, String prefix, Function<T, String> func)
    {
        if (array.length == 0)
        {
            return;
        }
        sb.append(prefix).append(System.lineSeparator());
        Arrays.stream(array).forEach(it -> sb.append(func.apply(it)).append(System.lineSeparator()));
    }

    private String getClassKey(ITestResult testResult)
    {
        return testResult.getMethod().getTestClass().getRealClass().getName();
    }

    private void addToTree(ITestResult testResult, Maybe<String> stepMaybe)
    {
        ITestContext testContext = testResult.getTestContext();

        Optional.ofNullable(ITEM_TREE.getTestItems().get(ItemTreeUtils.createKey(testContext.getSuite()))).flatMap(
                        suiteLeaf -> Optional.ofNullable(suiteLeaf.getChildItems().get(ItemTreeUtils.createKey(testContext)))
                                .flatMap(testLeaf -> Optional.ofNullable(
                                        testLeaf.getChildItems().get(ItemTreeUtils.createKey(testResult.getTestClass())))))
                .ifPresent(testClassLeaf -> testClassLeaf.getChildItems()
                        .put(ItemTreeUtils.createKey(testResult), TestItemTree.createTestItemLeaf(stepMaybe)));
    }

    private void addToTree(ITestContext testContext, Maybe<String> testId)
    {
        Optional.ofNullable(ITEM_TREE.getTestItems().get(ItemTreeUtils.createKey(testContext.getSuite())))
                .ifPresent(suiteLeaf ->
                {
                    List<XmlClass> testClasses = testContext.getCurrentXmlTest().getClasses();
                    ConcurrentHashMap<TestItemTree.ItemTreeKey, TestItemTree.TestItemLeaf> testClassesMapping =
                            new ConcurrentHashMap<>(
                            testClasses.size());
                    for (XmlClass testClass : testClasses)
                    {
                        TestItemTree.TestItemLeaf testClassLeaf = TestItemTree.createTestItemLeaf(testId,
                                new ConcurrentHashMap<>());
                        testClassesMapping.put(ItemTreeUtils.createKey(testClass), testClassLeaf);
                    }
                    suiteLeaf.getChildItems().put(ItemTreeUtils.createKey(testContext),
                            TestItemTree.createTestItemLeaf(testId, testClassesMapping));
                });
    }

    private void removeFromTree(ITestContext testContext)
    {
        Optional.ofNullable(ITEM_TREE.getTestItems().get(ItemTreeUtils.createKey(testContext.getSuite())))
                .ifPresent(suiteLeaf -> suiteLeaf.getChildItems().remove(ItemTreeUtils.createKey(testContext)));
    }

    private boolean hasMethodsToRun(ITestContext testContext)
    {
        return null != testContext && null != testContext.getAllTestMethods()
                && 0 != testContext.getAllTestMethods().length;
    }

    private String getRpLogItemUiLink(Launch launch, Long launchId, String path)
    {
        var parameters = launch.getParameters();
        String url = parameters.getBaseUrl();
        String modeType = parameters.getLaunchRunningMode() == Mode.DEBUG ? "userdebug" : "launches";
        String project = parameters.getProjectName();
        path = path.replace('.', '/');
        return String.format("%sui/#%s/%s/all/%s/%s/log", url, project, modeType, launchId, path);
    }

    private Set<ItemAttributesRQ> getAttributesFromClass(Class<?> clazz)
    {
        return Try.of(() -> AttributeParser.retrieveAttributes(clazz.getAnnotation(Attributes.class)))
                .getOrElse(HashSet::new);
    }

    private void logFile(File file, String message)
    {
        RP_BINARY_LOGGER.info("RP_MESSAGE#FILE#{}#{}", file.getAbsolutePath(), message);
    }

}
