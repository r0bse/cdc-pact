package de.schroeder.consumer.control

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.*
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.consumer.junit5.ProviderType
import au.com.dius.pact.core.model.PactSpecVersion
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import de.schroeder.consumer.ConsumerApplication
import de.schroeder.consumer.entity.CreateRequest
import feign.Request
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension


const val CONSUMER = "superhero-consumer-service"
const val PROVIDER = "superhero-provider-service"

const val GET_ALL = "at least one superhero exists"
const val GET_ONE = "a requested superhero exists"

const val CREATE_ONE = "a superhero, to be created, does not exist"

@PactTestFor(
    providerName = PROVIDER,
    port = "8081", // to use a random port delete this entry (or replace with "0", and rebuild the FeignClient with the mockServer port
    pactVersion = PactSpecVersion.V3
)
@ExtendWith(PactConsumerTestExt::class, SpringExtension::class)
@SpringBootTest(classes = [ConsumerApplication::class], webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ProviderClientConsumerTest {

    @Autowired
    lateinit var providerCLient: ProviderClient

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
            .method(Request.HttpMethod.GET.name)
            .headers(mapOf("foo" to "bar"))
            .willRespondWith()
            .headers(mapOf(HttpHeaders.CONTENT_TYPE to MediaType.APPLICATION_JSON_VALUE))
            .body(responsePayload)
            .status(HttpStatus.OK.value())
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "getOneSuperheroPact")
    fun `getting one superhero by id should succeed`(mockServer: MockServer) {
        providerCLient.getOne(42)
    }

    @Pact(
        consumer = CONSUMER,
        provider = PROVIDER
    )
    fun getAllSuperheroesPact(builder: PactDslWithProvider): RequestResponsePact {
        val responsePayload: DslPart = LambdaDsl.newJsonArrayMinLike(1) {
                payload: LambdaDslJsonArray ->
                payload.`object` { entry ->
                    entry.stringMatcher("name",".*","Peter Parker")
                    entry.stringMatcher("secretIdentity", ".*", "Spider-Man")
                    entry.stringMatcher("affiliation",".*", "Marvel")
                }
        }.build()

        return builder.given(GET_ALL)
            .uponReceiving("a request to get all superheroes")
            .path("/superheroes")
            .method(Request.HttpMethod.GET.name)
            .headers(mapOf("foo" to "bar"))
            .willRespondWith()
            .headers(mapOf(HttpHeaders.CONTENT_TYPE to MediaType.APPLICATION_JSON_VALUE))
            .body(responsePayload)
            .status(HttpStatus.OK.value())
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "getAllSuperheroesPact")
    fun `getting all superheroes should succeed`(mockServer: MockServer) {
        providerCLient.getAll()
    }

    @Pact(
        consumer = CONSUMER,
        provider = PROVIDER
    )
    fun createSuperheroPact(builder: PactDslWithProvider): RequestResponsePact {
        val requestPayload: DslPart = LambdaDsl.newJsonBody {
                payload: LambdaDslJsonBody ->
            payload.stringMatcher("name", ".*", "Peter Porker")
            payload.stringMatcher("secretIdentity", ".*", "Spider-Ham")
            payload.stringMatcher("affiliation", ".*", "Marvel")
        }.build()

        return builder.given(CREATE_ONE)
            .uponReceiving("a request to create a superhero")
            .path("/superheroes")
            .method(Request.HttpMethod.POST.name)
            .headers(mapOf(HttpHeaders.CONTENT_TYPE to MediaType.APPLICATION_JSON_VALUE))
            .body(requestPayload)
            .headers(mapOf("foo" to "bar"))
            .willRespondWith()
            .headers(mapOf(HttpHeaders.CONTENT_TYPE to MediaType.APPLICATION_JSON_VALUE))
            .status(HttpStatus.CREATED.value())
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "createSuperheroPact")
    fun `creating a superhero should succeed`(mockServer: MockServer) {
        providerCLient.create(CreateRequest("Peter Porker", "Marvel", "Spider-Ham"))
    }
}