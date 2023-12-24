import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.triggers.schedule
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2023.11"

project {

    vcsRoot(HttpsGithubComDzmitryasonauReportPortalAutomationGitRefsHeadsMain1)

    buildType(Daily_Run)
    buildType(Build)

    params {
        param("env.notifications.enabled", "true")
        param("env.rp.api.key", "4108ef3f-376e-4201-8bee-785654f23dc9")
        param("env.tms.apiKey", "QwqlTTu.ZNyl1l2zH83C-FSmASWM3y2E7tnGr36dy")
        param("env.aes.key", "lk4jFOTUvvKo8ptx0IGFLploseJU7OR8")
        param("env.browser.headless", "true")
        param("env.teams.webhook", "https://epam.webhook.office.com/webhookb2/da511ff3-bf18-4f49-9a21-96e1d9bea726@b41b72d0-4e9f-4c26-8a69-f949f367c91d/IncomingWebhook/fa83e7bc86414070a7149bd5282c7eb3/4492a597-cc63-496a-87ed-41ad91005c53")
        param("env.rp.enable", "true")
        param("env.tms.username", "dzmitry_asonau@epam.com")
        param("env.browser.name", "chrome")
        param("env.browser.run.type", "LOCAL")
        param("env.teams.enabled", "true")
    }
}

object Build : BuildType({
    name = "VCS Build"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        step {
            name = "SonarQubeScan"
            id = "SonerQubeScan"
            type = "sonar-plugin"
            param("sonarProjectSources", ".")
            param("teamcity.build.workingDir", ".")
            param("target.jdk.home", "%env.JDK_17_0%")
            param("teamcity.tool.sonarquberunner", "%teamcity.tool.sonar-qube-scanner.4.2.0.1873-scanner%")
            param("sonarProjectBinaries", ".")
            param("sonarServer", "5c177fd8-d4c7-409a-9ecb-2047996e226a")
        }
        gradle {
            id = "gradle_runner"
            tasks = "clean build"
            buildFile = "build.gradle"
            enableDebug = true
        }
    }

    triggers {
        vcs {
        }
        schedule {
            schedulingPolicy = daily {
                hour = 8
            }
            triggerBuild = always()
        }
    }

    features {
        perfmon {
        }
        pullRequests {
            vcsRootExtId = "${DslContext.settingsRoot.id}"
            provider = github {
                authType = token {
                    token = "credentialsJSON:b588e6cd-d2c1-4d32-9193-114cdb4a42d5"
                }
                filterAuthorRole = PullRequests.GitHubRoleFilter.MEMBER
                ignoreDrafts = true
            }
        }
        commitStatusPublisher {
            vcsRootExtId = "${DslContext.settingsRoot.id}"
            publisher = github {
                githubUrl = "https://api.github.com"
                authType = personalToken {
                    token = "credentialsJSON:b588e6cd-d2c1-4d32-9193-114cdb4a42d5"
                }
            }
        }
    }
})

object Daily_Run : BuildType({
    name = "Daily run"

    vcs {
        root(HttpsGithubComDzmitryasonauReportPortalAutomationGitRefsHeadsMain1)
    }

    steps {
        gradle {
            id = "gradle_runner"
            tasks = "clean build"
            gradleWrapperPath = ""
        }
    }

    triggers {
        schedule {
            triggerBuild = always()
            withPendingChangesOnly = false
        }
    }

    features {
        perfmon {
        }
    }
})

object HttpsGithubComDzmitryasonauReportPortalAutomationGitRefsHeadsMain1 : GitVcsRoot({
    name = "https://github.com/dzmitryasonau/report-portal-automation.git#refs/heads/main (1)"
    url = "https://github.com/dzmitryasonau/report-portal-automation.git"
    branch = "refs/heads/main"
    branchSpec = "refs/heads/*"
})
