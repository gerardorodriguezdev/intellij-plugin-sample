plugins {
    alias(libs.plugins.kotlin)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())
}