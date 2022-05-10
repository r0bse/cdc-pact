package de.schroeder.provider

import de.schroeder.provider.testutil.PostgresTestContainerTest
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@PostgresTestContainerTest
class ProviderApplicationTests {

    @Test
    fun contextLoads() {
    }

}
