import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

afterEvaluate {
    tasks.withType<KotlinCompile> {
        // overwrite default (1.4) to have access to kotlin.io.path APIs
        kotlinOptions.languageVersion = "1.5"
        kotlinOptions.apiVersion = "1.5"
    }
}
