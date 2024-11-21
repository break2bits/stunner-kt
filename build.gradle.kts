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
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "com.hal.stunner.MainKt"
}
//
//tasks.register<JavaExec>("start") {
//    mainClass = "com.hal.stunner.Main"
//    classpath =
//}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(22))
    }
}