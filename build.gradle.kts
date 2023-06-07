import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0-Beta"
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass = "MainKt"
}

tasks {
    withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget = JVM_17
            allWarningsAsErrors = true
            freeCompilerArgs.addAll("-progressive", "-Xcontext-receivers", "-Xsuppress-version-warnings")
        }
    }

    register<GenerateFilesAndBoilerplateForDayTask>("generate")
}
