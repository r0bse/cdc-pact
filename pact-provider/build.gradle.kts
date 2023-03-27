import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {

    System.setProperty("kotlinVersion", "1.8.10")
    System.setProperty("pactVersion", "4.5.4")
    System.setProperty("postgresVersion", "42.2.14")

    repositories {
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${System.getProperty("kotlinVersion")}")
        classpath("org.jetbrains.kotlin:kotlin-allopen:${System.getProperty("kotlinVersion")}")
        classpath("org.jetbrains.kotlin:kotlin-noarg:${System.getProperty("kotlinVersion")}")
    }
}

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version System.getProperty("kotlinVersion")
    kotlin("plugin.spring") version System.getProperty("kotlinVersion")
    kotlin("plugin.allopen") version System.getProperty("kotlinVersion")
    kotlin("plugin.jpa") version System.getProperty("kotlinVersion")
    id("au.com.dius.pact") version System.getProperty("pactVersion")
}

group = "de.schroeder"
version = "0.0.2-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17



repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.github.microutils:kotlin-logging:2.0.4")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.liquibase:liquibase-core")
    implementation("org.jetbrains.kotlin:kotlin-noarg:${System.getProperty("kotlinVersion")}")
    runtimeOnly("org.postgresql:postgresql:${System.getProperty("postgresVersion")}")


    testImplementation("io.mockk:mockk:1.10.6")
    testImplementation("com.ninja-squad:springmockk:3.0.1")
    testImplementation("org.testcontainers:testcontainers:1.15.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("au.com.dius.pact.provider:spring:${System.getProperty("pactVersion")}")
    testImplementation("au.com.dius.pact.provider:junit5:${System.getProperty("pactVersion")}")
    testImplementation("au.com.dius.pact.provider:junit5spring:${System.getProperty("pactVersion")}")
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
    systemProperty("pactbroker.host", "localhost:9292")
    systemProperty("pact.rootDir", "$buildDir/pacts")
    systemProperty("pact.verifier.publishResults", true)

    val pactTag = System.getProperty("pact.provider.tag")?: "dev"
    val projectVersion = System.getProperty("pact.provider.version")?: project.version
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