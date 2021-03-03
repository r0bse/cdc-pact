package de.schroeder.provider.boundary

import au.com.dius.pact.core.model.Interaction
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import au.com.dius.pact.provider.junitsupport.Consumer
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.State
import au.com.dius.pact.provider.junitsupport.loader.PactBroker
import au.com.dius.pact.provider.junitsupport.target.TestTarget
import au.com.dius.pact.provider.spring.target.MockMvcTarget
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget

const val GET_ALL = "at least one superhero exists"
const val GET_ONE = "a requested superhero exists"

const val CREATE_ONE = "a superhero, to be created, does not exist"

@Provider("pact-provider")
@Consumer("pact-consumer")
@PactBroker(
    host = "localhost",
    port = "8090"
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SuperheroEntityControllerProviderTest{

    @Autowired
    lateinit var mockMvc : MockMvc

    @BeforeEach
    fun before(context: PactVerificationContext) {
        context.target = MockMvcTestTarget(mockMvc)
    }

    @State(GET_ONE)
    fun `get one should succeed`() {
    }

    @State(GET_ALL)
    fun `get all should succeed`() {
    }

    @State(CREATE_ONE)
    fun `create one should succeed`() {
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider::class)
    fun testTemplate(context: PactVerificationContext) {
        context.verifyInteraction()
    }
}