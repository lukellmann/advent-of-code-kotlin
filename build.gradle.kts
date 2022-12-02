import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
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
        kotlinOptions {
            jvmTarget = "17"
            allWarningsAsErrors = true
            freeCompilerArgs += "-progressive"
        }
    }
}
