import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


fun properties(key: String) = project.findProperty(key).toString()

plugins {
    // Java support
    id("java")
    // Kotlin support
    // do NOT update kotlin - kotlin version must match platform version, see https://plugins.jetbrains.com/docs/intellij/kotlin.html#kotlin-standard-library
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij") version "1.8.0"
    // id("com.jetbrains.rdgen") version "2022.2.5"
    // gradle-changelog-plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
    id("org.jetbrains.changelog") version "1.3.1"
    // detekt linter - read more: https://detekt.github.io/detekt/gradle.html
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
    // ktlint linter - read more: https://github.com/JLLeitschuh/ktlint-gradle
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    // grammarkit to generate parser & lexer (i.e. the bnf and the flex file...)
    // ("org.jetbrains.grammarkit") version "2021.2.2"
}

val jvmVersion = "11"
val kotlinVersion = "1.6" // should match org.jetbrains.kotlin.jvm (major.minor)

group = properties("pluginGroup")
version = properties("pluginVersion")

// Configure project's dependencies
repositories {
    mavenCentral()
    jcenter()
}
dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
}

// for generated parser code..
sourceSets["main"].java.srcDirs("src/main/gen")

// Configure gradle-intellij-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))
    downloadSources.set(properties("platformDownloadSources").toBoolean())
    updateSinceUntilBuild.set(true)

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    plugins.set(
        properties("platformPlugins")
            .split(',')
            .map(String::trim)
            .filter(String::isNotEmpty)
    )
}

// Configure detekt plugin.
// Read more: https://detekt.github.io/detekt/kotlindsl.html
detekt {
    config = files("./detekt-config.yml")
    buildUponDefaultConfig = true

    reports {
        html.enabled = false
        xml.enabled = false
        txt.enabled = false
    }
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = jvmVersion
        targetCompatibility = jvmVersion
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = jvmVersion
            languageVersion = kotlinVersion
            apiVersion = kotlinVersion
            freeCompilerArgs = listOf("-Xjvm-default=compatibility")
        }
    }
    withType<Detekt> {
        jvmTarget = jvmVersion
        excludes.add("**/gen/**")
        reports {
            html.required.set(false)
            xml.required.set(false)
            txt.required.set(false)
        }
    }
    buildSearchableOptions {
        enabled = false
    }
    // workaround for https://youtrack.jetbrains.com/issue/IDEA-210683
    getByName<JavaExec>("buildSearchableOptions") {
        jvmArgs(
            // I gave up on tracking individual illegal access violations. 
            // This seems to be an integral and unfixable part of IntelliJ.
            "--illegal-access=permit"
        )
    }
    withType<org.jlleitschuh.gradle.ktlint.tasks.KtLintCheckTask> {
        exclude("**/gen/**", "**/*.Generated.kt")
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(
            provider {
                File(projectDir, "./plugin_description.md").readText().lines().run {
                    val start = "<!-- Plugin description -->"
                    val end = "<!-- Plugin description end -->"

                    if (!containsAll(listOf(start, end))) {
                        throw GradleException(
                            "Plugin description section not found in plugin_description.md:\n$start ... $end"
                        )
                    }
                    subList(indexOf(start) + 1, indexOf(end))
                }.joinToString("\n").run { markdownToHTML(this) }
            }
        )

        changeNotes.set(
            provider {
                File(projectDir, "./plugin_description.md").readText().lines().run {
                    val start = "<!-- Plugin changeNotes -->"
                    val end = "<!-- Plugin changeNotes end -->"

                    if (!containsAll(listOf(start, end))) {
                        throw GradleException(
                            "Plugin changeNotes section not found in plugin_description.md:\n$start ... $end"
                        )
                    }
                    subList(indexOf(start) + 1, indexOf(end))
                }.joinToString("\n").run { markdownToHTML(this) }
            }
        )
    }

    runPluginVerifier {
        // verifierVersion.set("1.256") // default ist "latest".
        ideVersions.addAll(
            properties("pluginVerifierIdeVersions")
                .split(',')
                .map(String::trim)
                .filter(String::isNotEmpty)
        )
        // reports are in ${project.buildDir}/reports/pluginVerifier - or set verificationReportsDirectory()
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
        channels.set(listOf(properties("marketplaceChannel")))
    }
}
