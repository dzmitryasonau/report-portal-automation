# Test Automation Framework for Report Portal

## 1. Required Software
- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) 
- [Gradle](https://gradle.org/)

## 2. Tools and libraries

- [Spring Framework](https://spring.io/projects/spring-framework)
- [Selenium](https://www.selenium.dev/) web UI tests.
- [Feign Client](https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-feign.html) web service client.
- [TestNG](https://testng.org/doc/) test runner.
- [Report Portal](https://reportportal.io/) tests reporting.
- [Sauce Labs](https://saucelabs.com/) application for cross-browser testing.

## 3. CI
As a CI tool will be used [CircleCI](https://app.circleci.com/pipelines/github/test-IO/test-automation).

## 4. Tests reporting

Test results are reported to the Report Portal and TestRail.

### Report Portal
[Report Portal automation dashboard](https://reportportal.epam.com/ui/#dzmitry_asonau_personal/dashboard/130717)

### TestRail
Test cases can be found here - [Test Cases](https://epmtio.testrail.io/index.php?/suites/view/355)

## 5. Local tests execution
For running the tests you need to create the file the property file under the next location (it's already under the .gitignore):

`/domain-module/src/main/resources/local.properties`

and define the next properties:

```
browser.run.type=      -   (mandatory) parameter for defining WebDriver type. Supported local execution type and remote (SuaceLabs). Availible values are: LOCAL, REMOTE
browser.accessKey=     -   (optional) access key parameter for SauceLabs. This applies only when browser.run.type=REMOTE
browser.username=      -   (optional) username parameter for SauceLabs. This applies only when browser.run.type=REMOTE

