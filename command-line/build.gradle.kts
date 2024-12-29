plugins {
    alias(libs.plugins.kotlin)
    application
}

group = "ttk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
    implementation(project(":code-generator"))

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

application {
    mainClass.set("MainKt")
}