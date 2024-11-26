plugins {
    kotlin("jvm") version "2.0.21"
    application
}

group = "com.hal"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    testImplementation(kotlin("test"))
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "com.hal.stunner.MainKt"
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(22))
    }
}