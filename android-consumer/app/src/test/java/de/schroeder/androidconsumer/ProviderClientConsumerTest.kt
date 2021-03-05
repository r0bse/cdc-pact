package de.schroeder.consumer.control

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.DslPart
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import com.google.common.collect.ImmutableMap
import de.schroeder.androidconsumer.ProviderClient
import de.schroeder.androidconsumer.RetrofitRequests
import de.schroeder.androidconsumer.TableService
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.pactfoundation.consumer.dsl.LambdaDsl
import io.pactfoundation.consumer.dsl.LambdaDslJsonArray
import io.pactfoundation.consumer.dsl.LambdaDslJsonBody
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

const val CONSUMER = "android-consumer"
const val PROVIDER = "pact-provider"

const val GET_ALL = "at least one superhero exists"
const val GET_ONE = "a requested superhero exists"

@PactTestFor(
    providerName = PROVIDER,
    port = "0" // random port
)
@ExtendWith(PactConsumerTestExt::class)
class ProviderClientConsumerTest {

    @MockK(relaxUnitFun = true)
    lateinit var tableService: TableService

    lateinit var requestClient: ProviderClient

    @BeforeEach
    fun setup(mockServer: MockServer){
        MockKAnnotations.init(this)

        requestClient = RetrofitRequests(tableService, "http://localhost:${mockServer.getPort()}").providerClient
    }

    @Pact(
        consumer = CONSUMER,
        provider = PROVIDER
    )
    fun getOneSuperheroPact(builder: PactDslWithProvider): RequestResponsePact {

        val responsePayload: DslPart = LambdaDsl.newJsonBody {
                payload: LambdaDslJsonBody ->
            payload.stringMatcher("name", ".*", "Bruce Wayne")
            payload.stringMatcher("secretIdentity", ".*", "Batman")
            payload.stringMatcher("affiliation", ".*", "DC")
        }.build()

        return builder.given(GET_ONE)
            .uponReceiving("a request to get a superhero by id")
            .path("/superheroes/42")
            .method("GET")
            .willRespondWith()
            .headers(ImmutableMap.of("Content-Type", "application/json"))
            .body(responsePayload)
            .status(200)
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "getOneSuperheroPact")
    fun `getting one superhero by id should succeed`(mockServer: MockServer) {
        requestClient.getSuperhero(42).execute()
    }

    @Pact(
        consumer = CONSUMER,
        provider = PROVIDER
    )
    fun getAllSuperheroesPact(builder: PactDslWithProvider): RequestResponsePact {
        val responsePayload: DslPart = LambdaDsl.newJsonArrayMinLike(1) { payload: LambdaDslJsonArray ->
                payload.`object` { entry ->
                    entry.stringMatcher("name", ".*", "Peter Parker")
                    entry.stringMatcher("secretIdentity", ".*", "Spider-Man")
                    entry.stringMatcher("affiliation", ".*", "Marvel")
                }
        }.build()

        return builder.given(GET_ALL)
            .uponReceiving("a request to get all superheroes")
            .path("/superheroes")
            .method("GET")
            .willRespondWith()
            .headers(ImmutableMap.of("Content-Type", "application/json"))
            .body(responsePayload)
            .status(200)
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "getAllSuperheroesPact")
    fun `getting all superheroes should succeed`(mockServer: MockServer) {
        requestClient.getSuperheroes().execute()
    }
}