# Test Automation Framework for Report Portal

The test automation framework for Report Portal is a robust and modularized solution designed to test APIs, UI functionality, and integrations with high reusability and efficiency. It leverages widely-used tools, libraries, and design principles to offer scalable and maintainable test automation for projects of varying complexity.

---

## 1. Required Software

Ensure the following software is installed before using the framework:

- [JDK 21](https://adoptium.net/temurin/releases/?version=21) - Java Development Kit for compiling and running the framework.
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) - Integrated Development Environment for development and test execution.
- [Gradle](https://gradle.org/) - Build automation tool used for dependencies management and task automation.

---

## 2. Framework Features and Architecture

### Key Features:
- **Multi-Layered Modular Design**: Separates `core-module` (shared components, utilities, API/services) and `tests-module` (test cases, step definitions, features).
- **Cross-Browser UI Testing**: Supports Selenium/Selenide-based testing for Web UI on popular browsers: Chrome, Firefox, and Safari.
- **API Testing Support**: Enables seamless REST API testing with **Rest-Assured** and **Feign Client** for HTTP request handling.
- **BDD Support**: Out-of-the-box support for **Cucumber** with `.feature` files to write scenarios for UI and API tests.
- **Parallel Testing**: Utilizes TestNG/JUnit5 for efficient test execution, including parallel runs.
- **Dynamic Configuration Management**: Configuration-driven approach using `local.properties` allows flexible control over test environment setups (e.g., browser type, SauceLabs credentials, etc.).
- **Error Logging**: Takes screenshots on test failures and adds detailed logs for debugging.
- **Test Reporting and Integration**: Integrated with **Allure Reports** and **Report Portal** for visual test execution results and analytics.
- **CI/CD Compatible**: Configured to work with GitHub Actions for automated, headless test execution in CI/CD pipelines.

---

## 3. Tools and Libraries

The framework is built using industry-standard tools and libraries:

- [Spring Framework](https://spring.io/projects/spring-framework): Handles dependency injection and configurations.
- [Selenium](https://www.selenium.dev/): Provides WebDriver for browser-based UI testing.
- [Selenide](https://selenide.org/): Simplifies browser control and UI interactions (used as a wrapper around Selenium).
- [Rest-Assured](https://rest-assured.io/): For API testing.
- [Feign Client](https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-feign.html): HTTP client for REST API calls.
- [TestNG](https://testng.org/doc/): For test orchestration and execution.
- [JUnit5](https://junit.org/junit5/): Alternative test orchestration library supported by the framework.
- [Allure](https://docs.qameta.io/allure/): For visual test reporting.
- [Report Portal](https://reportportal.io/): Centralized reporting and test analytics.

Optional Dependencies:
- **Remote Browser Support via SauceLabs** for executing tests in remote environments.

---

## 4. Continuous Integration (CI)

The project uses **GitHub Actions** for Continuous Integration. The GitHub workflows are defined in the `.github/workflows` directory:
- **`pr_trigger.yml`**: Triggers test execution upon pull requests to validate changes.
- **`pr_auto_merge_actions.yml`**: Automatically merges pull requests after successful builds and tests.

---

## 5. Framework Structure and Overview

The framework follows a layered architecture. Below is an overview of the key components:

### 1. `core-module`
- **Purpose**: Houses reusable components required for test automation, including utilities, configurations, and interactions.
- **Key Packages**:
  - `api`: API clients, configurations for REST API testing.
  - `ui`: Page Objects, browser strategies, and UI test helpers.
  - `service`: Shared logic for data processing and services.
  - `utils`: Utility classes for common operations like retries, custom object formatting, and more.
  - `resources`: Environment-specific configuration files (e.g., `application.properties`) and test data.

---

### 2. `tests-module`
- **Purpose**: Contains implementation of test cases, step definitions (for BDD), and test configurations.
- **Key Components**:
  - Step Definitions: Features step definitions for both API and UI tests.
  - Test Cases: Structured test scenarios for specific testing needs, organized by test runners (JUnit, TestNG, Cucumber, etc.).
  - Reporting and Screenshots: Integration with Allure and Report Portal for test results. Stores screenshots for failed tests.
  - `testng.xml`: TestNG configuration files for running tests.

---

## 6. Local Test Execution

### Property Configuration
To run tests locally, create a `local.properties` file in the directory:  
`/core-module/src/main/resources/local.properties`.

Define the following properties:

```properties
# WebDriver settings
browser.run.type=          # (mandatory) Execution type. Available values: LOCAL, REMOTE
browser.name=              # (mandatory) Browser type. Available values: chrome, firefox, safari
browser.accessKey=         # (optional) Access key for SauceLabs. Applicable only for REMOTE type.
browser.username=          # (optional) Username for SauceLabs. Applicable only for REMOTE type.

# Secure Data Management
aes.key=                   # (mandatory) Cipher key for decrypting test user secrets. Obtain from a **Dzmitry Asonau**.
```

### Sample Configuration
For local execution on Chrome:

```properties
browser.run.type=LOCAL
browser.name=chrome
aes.key=example-secret-key
```

For remote execution on SauceLabs:

```properties
browser.run.type=REMOTE
browser.name=chrome
browser.accessKey=your-access-key
browser.username=your-username
aes.key=example-secret-key
```

### Running the Tests
1. Open the project in IntelliJ IDEA.
2. Ensure `local.properties` is defined correctly with desired configurations.
3. Use the following Gradle commands:
  - To run all tests:
    ```bash
    ./gradlew test
    ```
  - To run a specific test suite/module:
    ```bash
    ./gradlew :tests-module:test
    ```
4. Reports will be generated in `build/reports/tests/`.

---

## 7. Reporting and Debugging

### Allure Reports
Allure results are stored in the `/allure-results` folder and can be converted to a visual report using:
```bash
allure serve allure-results
```

### Report Portal
The framework automatically integrates with Report Portal for test result reporting. Ensure Report Portal configurations are correctly set in `reportportal.properties`.

---

## 8. Framework's Extensibility

### Adding New Tests
1. Add a new `.feature` file if using BDD (e.g., `tests-module/src/test/resources/features`).
2. Define Step Definitions in the `step_definitions` package.
3. Add corresponding API or UI action implementations in the `core-module`.

### Adding New Features
The framework supports a modular approach. Add shared utilities or logic to `core-module` for reusability across `tests-module`.

---

## 9. Troubleshooting

1. **Missing Dependencies**: Run `./gradlew clean build --refresh-dependencies` to ensure all dependencies are up to date.
2. **Incorrect Browser Configuration**: Verify `local.properties` for valid browser names and execution types.
3. **Decryption Issues**: Confirm `aes.key` is set correctly and matches the team-provided key.

---

For additional questions, contact **Dzmitry Asonau**.
