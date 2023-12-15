plugins {
    kotlin("jvm") version "2.0.0-Beta2"
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass = "MainKt"
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        allWarningsAsErrors = true
        progressiveMode = true
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

tasks {
    register<GenerateFilesAndBoilerplateForDayTask>("generate")
}
