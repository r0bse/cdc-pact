import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.spring") version "1.8.10"
    id ("au.com.dius.pact") version "4.1.19"
}

group = "de.schroeder"
version = "0.0.3-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2022.0.1"
extra["pactVersion"] = "4.1.19"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("au.com.dius.pact.consumer:junit5:${property("pactVersion")}")
    testImplementation("au.com.dius.pact.consumer:java8:${property("pactVersion")}")
    testImplementation("io.mockk:mockk:1.10.5")
    testImplementation("com.ninja-squad:springmockk:3.0.1")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("pact.rootDir", "$buildDir/pacts")
    systemProperty("pact.verifier.publishResults", true)

    systemProperty("pact.provider.version", System.getProperty("pact.provider.version"))?: project.version
    systemProperty("pact.provider.tag", System.getProperty("pact.provider.tag")) // how should a verified providertest be tagged?
    systemProperty("pact.showFullDiff", true)
}

/**
 * Publishing contracts to pactbroker configuration
 */
pact {
    publish {
        pactDirectory = "$buildDir/pacts"
        tags = listOf("dev") //how should the ConsumerPacts (of this service) be tagged
    }
    broker{
        pactBrokerUrl = "http://localhost:9292"
        retryCountWhileUnknown=0
        retryWhileUnknownInterval=0
    }
}