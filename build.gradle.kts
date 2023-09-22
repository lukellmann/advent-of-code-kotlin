import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17

plugins {
    kotlin("jvm") version "1.9.20-Beta2"
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
        jvmTarget = JVM_17
        allWarningsAsErrors = true
        progressiveMode = true
        freeCompilerArgs.addAll("-Xcontext-receivers", "-Xsuppress-version-warnings")
    }
}

tasks {
    register<GenerateFilesAndBoilerplateForDayTask>("generate")
}
