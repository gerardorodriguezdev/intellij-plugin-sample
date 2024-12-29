import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
  id("java")
  alias(libs.plugins.kotlin)
  id("org.jetbrains.intellij") version "1.15.0"
  id("org.jetbrains.compose") version "1.5.0-beta01"
}

group = "ttk"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
  maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  google()
}

intellij {
  version.set("2022.2.5")
  type.set("IC")
}

kotlin {
  jvmToolchain(libs.versions.jvm.get().toInt())
}

dependencies {
  implementation(project(":code-generator"))

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.2")
  implementation(compose.desktop.macos_arm64)
  implementation(compose.materialIconsExtended)
  implementation(compose.uiTooling)

  testImplementation(kotlin("test"))
  testImplementation(libs.mockk)

  @OptIn(ExperimentalComposeLibrary::class)
  testImplementation(compose.uiTestJUnit4)
}

tasks {

  buildSearchableOptions {
    enabled = false
  }

  patchPluginXml {
    sinceBuild.set("222")
    untilBuild.set("242.*")
  }

  signPlugin {
    certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
    privateKey.set(System.getenv("PRIVATE_KEY"))
    password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
  }

  publishPlugin {
    token.set(System.getenv("PUBLISH_TOKEN"))
  }
}
