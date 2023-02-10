import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.20-Beta"
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("MainKt")
}

tasks {

    // To upgrade Gradle, run the following command twice and commit all changes:
    // ./gradlew wrapper --gradle-version <version> --gradle-distribution-sha256-sum <checksum>
    // (use 'Complete (-all) ZIP Checksum' from https://gradle.org/release-checksums for <checksum>)
    wrapper {
        distributionType = ALL
    }

    withType<KotlinCompile> {
        compilerOptions {
            languageVersion.set(KOTLIN_2_0) // enable K2 compiler
            freeCompilerArgs.add("-Xsuppress-version-warnings")

            jvmTarget.set(JVM_17)
            allWarningsAsErrors.set(true)
            freeCompilerArgs.addAll(
                "-progressive",
                "-Xcontext-receivers",
                "-opt-in=kotlin.ExperimentalStdlibApi",
                "-opt-in=kotlin.time.ExperimentalTime",
            )
        }
    }

    register<GenerateFilesAndBoilerplateForDayTask>("generate")
}
