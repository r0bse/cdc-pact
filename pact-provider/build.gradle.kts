import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream

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
version = "0.0.3"
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

    /**
     * Gradle and Maven do not pass in the system properties in to the test JVM from the command line.
     * The system properties specified on the command line only control the build JVM (the one that runs Gradle or Maven),
     * but the tests will run in a new JVM.
     * Therefor all properties must be set via "systemProperty"
     */
    systemProperty("pactbroker.host", "localhost:9292") // set up in compose.yml
    systemProperty("pact.rootDir", "$buildDir/pacts")
    systemProperty("pact.verifier.publishResults", true) // wether testresults should be reported to pactBroker
    systemProperty("pact.showFullDiff", true)

    systemProperty("pact.provider.version", "${project.version}@${getGitHash()}") // version needs to be unique in PactBroker
    systemProperty("pact.provider.branch", gitBranch()) // the current branch running the pact
    systemProperty("pactbroker.providerTags", "master") // the branch which is allowed to report that a pact is verified on prod (removes PENDING state)
    systemProperty("pactbroker.enablePending", true) // wether Pending Pacts are activated
}

/**
 * enable jpa default constructors
 */
allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

/**
 * Utility function to retrieve the name of the current git branch.
 * Will not work if build tool detaches head after checkout, which some do!
 */
fun gitBranch(): String {
    return try {
        val byteOut = ByteArrayOutputStream()
        project.exec {
            commandLine = "git rev-parse --abbrev-ref HEAD".split(" ")
            standardOutput = byteOut
        }
        String(byteOut.toByteArray()).trim().also {
            if (it == "HEAD")
                logger.warn("Unable to determine current branch: Project is checked out with detached head!")
        }
    } catch (e: Exception) {
        logger.warn("Unable to determine current branch: ${e.message}")
        "Unknown Branch"
    }
}

fun getGitHash(): String {
    val stdout = ByteArrayOutputStream()
    project.exec {
        commandLine = "git rev-parse --short HEAD".split(" ")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}