import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("maven-publish")
    java
}

group = "me.redtea.kotlinbukkit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven { url = uri("https://repo.mattstudios.me/artifactory/public/") }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("me.mattstudios.utils:matt-framework:1.4.6")
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    implementation("dev.triumphteam:triumph-gui:3.1.3")

    compileOnly("org.jetbrains.exposed", "exposed-core", "0.40.1")
    compileOnly("org.jetbrains.exposed", "exposed-dao", "0.40.1")
    compileOnly("org.jetbrains.exposed", "exposed-jdbc", "0.40.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    implementation("net.kyori:adventure-text-minimessage:4.12.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
