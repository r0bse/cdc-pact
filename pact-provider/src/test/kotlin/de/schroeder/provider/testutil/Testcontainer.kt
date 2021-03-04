package de.schroeder.provider.testutil

import mu.KotlinLogging
import org.slf4j.LoggerFactory
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration(initializers = [PostgresTestContainerRunner::class])
annotation class PostgresTestContainerTest

class PostgresTestContainerRunner : ApplicationContextInitializer<ConfigurableApplicationContext> {

    private val logger = KotlinLogging.logger { }

    override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
        val dockerRegistry = configurableApplicationContext.environment.getProperty("testcontainer.dockerRegistryUri")
        val dockerImageName = configurableApplicationContext.environment.getProperty("testcontainer.dockerImageName")
        val dbPort = configurableApplicationContext.environment.getProperty("postgresql.port")
        val dbName = configurableApplicationContext.environment.getProperty("postgresql.db.name")!!

        logger.debug(String.format("Pulling Testcontainerimage from: %s", dockerRegistry))
        logger.debug(String.format("Pulling Image with name: %s", dockerImageName))
        logger.debug(String.format("Creating database: %s:%s", dbName, dbPort))

        val dbContainer: GenericContainer<*> = start(dockerRegistry + dockerImageName, dbName, Integer.valueOf(dbPort))
        val containerDbPort: Int = dbContainer.getMappedPort(Integer.valueOf(dbPort))
        val containerIpAddress: String = dbContainer.getContainerIpAddress()
        val values = TestPropertyValues.of(
            "postgresql.host=$containerIpAddress",
            "postgresql.port=$containerDbPort"
        )
        values.applyTo(configurableApplicationContext)
    }

    companion object {
        private lateinit var genericContainer: GenericContainer<*>

        /**
         * [GenericContainer.start] should be run in a static method otherwise testcontainer will start and stop a
         * container for each test class
         * see -> https://www.testcontainers.org/test_framework_integration/manual_lifecycle_control/#singleton-containers
         */
        private fun start(dockerImageName: String, dbName: String, dbPort: Int): GenericContainer<*> {
            genericContainer = GenericContainer(dockerImageName)
                .withExposedPorts(dbPort)
                .withEnv("POSTGRES_DATABASES", dbName)
                .withEnv("POSTGRES_PASSWORD", "password")
                .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 2))
            genericContainer.start()
            return genericContainer
        }
    }
}
