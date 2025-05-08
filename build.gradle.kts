import org.jetbrains.compose.desktop.*

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("org.jetbrains.compose") version "1.8.0-beta02"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.20"
}

group = "org.graphs.analyzer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(compose.desktop.currentOs)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
