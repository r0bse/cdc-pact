package de.schroeder.provider.testutil

import mu.KotlinLogging
import org.slf4j.LoggerFactory
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
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

        val dbContainer = start(dockerRegistry + dockerImageName, dbName, Integer.valueOf(dbPort))
        val containerDbPort: Int = dbContainer.getMappedPort(Integer.valueOf(dbPort))
        val containerIpAddress: String = dbContainer.containerIpAddress
        val values = TestPropertyValues.of(
            "postgresql.host=$containerIpAddress",
            "postgresql.port=$containerDbPort"
        )
        values.applyTo(configurableApplicationContext)
    }

    companion object {
        private lateinit var container: PostgreSQLContainer<Nothing>

        /**
         * [GenericContainer.start] should be run in a static method otherwise testcontainer will start and stop a
         * container for each test class
         */
        private fun start(dockerImageName: String, dbName: String, dbPort: Int): PostgreSQLContainer<*> {
            val container = PostgreSQLContainer(DockerImageName.parse("postgres:13-alpine"))
                .withDatabaseName(dbName)
                .withUsername("postgres")
                .withPassword("password")
            .withExposedPorts(dbPort)
            container.start()
            return container
        }

        /**
         * overwrite spring application settings
         */
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl);
            registry.add("spring.datasource.password", container::getPassword);
            registry.add("spring.datasource.username", container::getUsername);
        }
    }
}
