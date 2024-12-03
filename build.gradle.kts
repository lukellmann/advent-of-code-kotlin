import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_23

plugins {
    kotlin("jvm") version "2.1.0"
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass = "MainKt"
}

kotlin {
    compilerOptions {
        allWarningsAsErrors = true
        progressiveMode = true
        extraWarnings = true
        jvmTarget = JVM_23
        freeCompilerArgs.addAll(
            "-Xjdk-release=23",
            "-Xwhen-guards",
            "-Xcontext-receivers",
            "-Xsuppress-warning=CONTEXT_RECEIVERS_DEPRECATED",
            "-Xsuppress-warning=UNUSED_ANONYMOUS_PARAMETER",
        )
    }
}

tasks {
    register<GenerateFilesAndBoilerplateForDayTask>("generate")
    withType<JavaCompile> { options.release = 23 }
}
