import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
        classpath("org.jetbrains.kotlin:kotlin-allopen:1.8.10")
        classpath("org.jetbrains.kotlin:kotlin-noarg:1.8.10")
    }
}

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.spring") version "1.8.10"
    kotlin("plugin.allopen") version "1.8.10"
    kotlin("plugin.jpa") version "1.8.10"
    id("au.com.dius.pact") version "4.1.19"
}

group = "de.schroeder"
version = "0.0.2-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17



extra["pactVersion"] = "4.1.19"
extra["kotlinVersion"] = "1.8.10"
extra["postgresVersion"] = "42.2.14"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.github.microutils:kotlin-logging:2.0.4")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.liquibase:liquibase-core")
    implementation("org.jetbrains.kotlin:kotlin-noarg:${property("kotlinVersion")}")
    runtimeOnly("org.postgresql:postgresql:${property("postgresVersion")}")


    testImplementation("io.mockk:mockk:1.10.6")
    testImplementation("com.ninja-squad:springmockk:3.0.1")
    testImplementation("org.testcontainers:testcontainers:1.15.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("au.com.dius.pact.provider:spring:${property("pactVersion")}")
    testImplementation("au.com.dius.pact.provider:junit5:${property("pactVersion")}")
    testImplementation("au.com.dius.pact.provider:junit5spring:${property("pactVersion")}")
    testImplementation ("org.awaitility:awaitility:4.0.3")
    testImplementation("org.testcontainers:postgresql:1.15.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("pactbroker.host", "localhost:8090")
    systemProperty("pact.rootDir", "$buildDir/pacts")
    systemProperty("pact.verifier.publishResults", true)

    val pactTag = System.getProperty("pact.provider.tag")?: "wip"
    val projectVersion = System.getProperty("ppact.provider.version")?: project.version
    systemProperty("pact.provider.version", "$projectVersion")
    systemProperty("pact.provider.tag", pactTag) // how should a verified providertest be tagged?
    systemProperty("pact.showFullDiff", true)
}

/**
 * enable jpa default constructors
 */
allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}