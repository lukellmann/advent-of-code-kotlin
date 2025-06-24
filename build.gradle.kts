import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_24

plugins {
    kotlin("jvm") version "2.2.0"
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
        jvmTarget = JVM_24
        freeCompilerArgs.addAll(
            "-Xjdk-release=24",
            "-Xcontext-parameters",
            "-Xnested-type-aliases",
            "-Xcontext-sensitive-resolution",
        )
    }
}

tasks {
    register<GenerateFilesAndBoilerplateForDayTask>("generate")
    withType<JavaCompile> { options.release = 24 }
}
