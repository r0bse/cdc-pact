package de.schroeder.consumer.control

import android.widget.TableLayout
import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.DslPart
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import com.android.volley.Request
import com.google.common.collect.ImmutableMap
import de.schroeder.androidconsumer.RequestService
import de.schroeder.androidconsumer.TableService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.pactfoundation.consumer.dsl.LambdaDsl
import io.pactfoundation.consumer.dsl.LambdaDslJsonArray
import io.pactfoundation.consumer.dsl.LambdaDslJsonBody
import org.junit.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

const val CONSUMER = "android-consumer"
const val PROVIDER = "pact-provider"

const val GET_ALL = "at least one superhero exists"
const val GET_ONE = "a requested superhero exists"

@PactTestFor(
    providerName = PROVIDER,
    port = "8081" // to use a random port delete this entry (or replace with "0", and rebuild the FeignClient with the mockServer port
)
@ExtendWith(PactConsumerTestExt::class)
class ProviderClientConsumerTest {

    @MockK(relaxUnitFun = true)
    lateinit var tableService: TableService
    @MockK(relaxUnitFun = true)
    lateinit var tableLayout: TableLayout

    @InjectMockKs
    lateinit var requestService: RequestService

    @BeforeEach
    fun setup(){
        MockKAnnotations.init(this)
        every{ tableLayout.context } returns null
    }

    val url: String = "localhost"

    @Pact(
        consumer = CONSUMER,
        provider = PROVIDER
    )
    fun getOneSuperheroPact(builder: PactDslWithProvider): RequestResponsePact {

        val responsePayload: DslPart = LambdaDsl.newJsonBody {
                payload: LambdaDslJsonBody ->
            payload.stringMatcher("name", ".*", "Bruce Wayne")
            payload.stringMatcher("identity", ".*", "Batman")
            payload.stringMatcher("affiliation", ".*", "DC")
        }.build()

        return builder.given(GET_ONE)
            .uponReceiving("a request to get a superhero by id")
            .path("/superheroes/42")
            .method(Request.Method.GET.toString())
            .headers(ImmutableMap.of("foo", "bar"))
            .willRespondWith()
            .headers(ImmutableMap.of("Content-Type", "application/json"))
            .body(responsePayload)
            .status(200)
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "getOneSuperheroPact")
    fun `getting one superhero by id should succeed`(mockServer: MockServer) {
        requestService.callProvider(tableLayout)
    }

//    @Pact(
//        consumer = CONSUMER,
//        provider = PROVIDER
//    )
//    fun getAllSuperheroesPact(builder: PactDslWithProvider): RequestResponsePact {
//        val responsePayload: DslPart = LambdaDsl.newJsonArrayMinLike(1) {
//                payload: LambdaDslJsonArray ->
//                payload.`object` { entry ->
//                    entry.stringMatcher("name",".*","Peter Parker")
//                    entry.stringMatcher("identity", ".*", "Spider-Man")
//                    entry.stringMatcher("affiliation",".*", "Marvel")
//                }
//        }.build()
//
//        return builder.given(GET_ALL)
//            .uponReceiving("a request to get all superheroes")
//            .path("/superheroes")
//            .method("GET")
//            .headers(ImmutableMap.of("foo", "bar", "Content-Type", "application/json"))
//            .willRespondWith()
//            .headers(ImmutableMap.of("Content-Type", "application/json"))
//            .body(responsePayload)
//            .status(200)
//            .toPact()
//    }
//
//    @Test
//    @PactTestFor(pactMethod = "getOneSuperheroPact")
//    fun `getting one superhero by id should succeed`(mockServer: MockServer) {
//        requestService.callProvider(null)
//    }
}