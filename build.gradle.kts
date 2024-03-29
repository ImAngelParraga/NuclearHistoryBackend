import com.google.cloud.tools.gradle.appengine.appyaml.AppEngineAppYamlExtension

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_version: String by project
val ehcache_version: String by project
val mockkVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.2"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
    id("com.google.cloud.tools.appengine") version "2.4.3"
}

group = ""
version = "1.1"

application {
    mainClass.set("com.angelparraga.ApplicationKt")
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
    maven(url = "https://jitpack.io")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

kotlin {
    jvmToolchain(17)
}

configure<AppEngineAppYamlExtension> {
    stage {
        setArtifact("build/libs/${project.name}-all.jar")
    }
    deploy {
        version = "GCLOUD_CONFIG"
        projectId = "GCLOUD_CONFIG"
    }
}

dependencies {
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-serialization-gson:$ktor_version")

    // Koin for Kotlin apps
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    // Mongo
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.11.0")

    // Auth
    implementation("io.ktor:ktor-server-auth:$ktor_version")

    // Cache
    implementation("org.ehcache:ehcache:$ehcache_version")

    // Call logging
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")
    implementation("io.ktor:ktor-server-double-receive:$ktor_version")

    // CORS
    implementation("io.ktor:ktor-server-cors:$ktor_version")

    // Mockk
    testImplementation("io.mockk:mockk:${mockkVersion}")

}