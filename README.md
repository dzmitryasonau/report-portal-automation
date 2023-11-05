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
- or
- [JUnit5](https://junit.org/junit5/) test runner.
- [Report Portal](https://reportportal.io/) tests reporting.

## 3. CI
As a CI tool used [GitHub action](https://github.com/features/actions).

## 4. Local tests execution
For running the tests you need to create the file the property file under the next location (it's already under the .gitignore):

`/core-module/src/main/resources/local.properties`

and define the next properties:

```
browser.run.type=      -   (mandatory) parameter for defining WebDriver type. Supported local execution type and remote (SauceLabs). Availible values are: LOCAL, REMOTE
browser.accessKey=     -   (optional) access key parameter for SauceLabs. This applies only when browser.run.type=REMOTE
browser.username=      -   (optional) username parameter for SauceLabs. This applies only when browser.run.type=REMOTE

