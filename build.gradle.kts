val linter by configurations.creating

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("org.jetbrains.compose") version "1.8.0-beta02"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.20"
    jacoco
}

group = "org.graphs.analyzer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    google()
}

configurations.all {
    exclude(group = "net.java.dev", module = "stax-utils")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(compose.desktop.currentOs)
    implementation("org.xerial:sqlite-jdbc:3.49.1.0")
    linter("com.pinterest.ktlint:ktlint-cli:1.5.0")
    implementation("org.gephi:gephi-toolkit:0.10.1")
    implementation(files("./gephi_tools/org-gephi-graph-api.jar"))
    implementation(files("./gephi_tools/org-gephi-statistics-api.jar"))
    implementation(files("./gephi_tools/org-gephi-statistics-plugin.jar"))
    implementation(files("./gephi_tools/org-gephi-utils-longtask.jar"))
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

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoReports/")
    }
}

tasks.test {
    dependsOn(lintCheck)
    dependsOn(format)
    finalizedBy(tasks.jacocoTestReport)
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
