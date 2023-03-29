import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream

buildscript {

    System.setProperty("kotlinVersion", "1.8.10")
    System.setProperty("springCloudVersion", "2022.0.1")
    System.setProperty("pactVersion", "4.5.4")
}

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version System.getProperty("kotlinVersion")
    kotlin("plugin.spring") version System.getProperty("kotlinVersion")
    id ("au.com.dius.pact") version System.getProperty("pactVersion")
}

group = "de.schroeder"
version = "0.0.4"
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

springBoot {
    buildInfo()
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("au.com.dius.pact.consumer:junit5:${System.getProperty("pactVersion")}")
    testImplementation("io.mockk:mockk:1.10.5")
    testImplementation("com.ninja-squad:springmockk:3.0.1")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${System.getProperty("springCloudVersion")}")
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
}

val deployEnvironment = System.getProperty("deployEnvironment")?: "prod"
val pactConsumerVersion = "${project.version}@${getGitHash()}"

/**
 * um canIdeploy ausführen zu können, muss ein Pact für den Consumer existieren
 * da die Version den GIT-Hash beinhaltet, muss vorher ein pactPublish erfolgen
 */
tasks.canIDeploy.configure {
    pacticipant.set("superhero-consumer-service")
    pacticipantVersion.set(pactConsumerVersion)
    toProp.set(deployEnvironment) // prüft auf das Pact-Tag ab (alle Pacts die damit getagged und die letzte Version des Tags sind)
}

/**
 * Publishing contracts to pactbroker configuration
 */
pact {
    publish {
        pactDirectory = "$buildDir/pacts"
        tags = listOf(deployEnvironment)
        consumerBranch = gitBranch()
        consumerVersion = pactConsumerVersion
    }
    broker{
        pactBrokerUrl = "http://localhost:9292"
        retryCountWhileUnknown=0
        retryWhileUnknownInterval=0
    }
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