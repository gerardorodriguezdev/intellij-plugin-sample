plugins {
    alias(libs.plugins.kotlin)
    kotlin("plugin.serialization") version "1.8.21"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    testImplementation(kotlin("test"))
    testImplementation(project(":testing"))
    testImplementation(libs.mockk)
}

tasks.test {
    useJUnitPlatform {
        excludeTags("EndToEnd")
    }
}

tasks.register<Test>("testEndToEnd") {
    useJUnitPlatform {
        includeTags("EndToEnd")
    }
}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())
}