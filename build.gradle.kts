plugins {
    kotlin("jvm") version "1.9.20"
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
        freeCompilerArgs.addAll("-Xcontext-receivers", "-Xsuppress-version-warnings")
    }
}

tasks {
    register<GenerateFilesAndBoilerplateForDayTask>("generate")
}
