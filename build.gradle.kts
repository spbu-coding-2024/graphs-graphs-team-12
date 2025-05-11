import org.jetbrains.compose.desktop.*

val linter by configurations.creating

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
    linter("com.pinterest.ktlint:ktlint-cli:1.5.0")
}

val lintCheck by tasks.registering(JavaExec::class) {
    classpath = linter
    mainClass.set("com.pinterest.ktlint.Main")
}

val format by tasks.registering(JavaExec::class) {
    classpath = linter
    mainClass.set("com.pinterest.ktlint.Main")
    args = listOf<String>("--format")
}

tasks.test {
    dependsOn(lintCheck)
    dependsOn(format)
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
