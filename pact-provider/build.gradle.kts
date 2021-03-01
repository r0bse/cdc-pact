import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.30"
    kotlin("plugin.spring") version "1.4.30"
    kotlin("plugin.allopen") version "1.3.61"
    id("au.com.dius.pact") version "4.1.6"
}

group = "de.schroeder"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11
extra["pactVersion"] = "4.1.17"


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
//    implementation("org.liquibase:liquibase-core")
    implementation("org.jetbrains.kotlin:kotlin-noarg:1.4.30")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("au.com.dius.pact.provider:spring:${property("pactVersion")}")
    testImplementation("au.com.dius.pact.provider:junit5:${property("pactVersion")}")
    testCompile("au.com.dius.pact.provider:junit5spring:${property("pactVersion")}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
//    systemProperty("pactbroker.host", "localhost:8090")
    systemProperty("pact.rootDir", "$buildDir/pacts")
    systemProperty("pact.verifier.publishResults", true)

    systemProperty("pact.provider.tag", System.getProperty("pact.provider.tag")) // how should a verified providertest be tagged?
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