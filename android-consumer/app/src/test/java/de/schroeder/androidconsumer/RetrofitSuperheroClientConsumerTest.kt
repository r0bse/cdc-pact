package de.schroeder.androidconsumer

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.DslPart
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import com.google.common.collect.ImmutableMap
import com.google.gson.GsonBuilder
import de.schroeder.androidconsumer.superheroes.control.SuperheroClient
import io.mockk.MockKAnnotations
import io.pactfoundation.consumer.dsl.LambdaDsl
import io.pactfoundation.consumer.dsl.LambdaDslJsonArray
import io.pactfoundation.consumer.dsl.LambdaDslJsonBody
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val CONSUMER = "superhero-android-consumer"
const val PROVIDER = "superhero-provider-service"

const val GET_ALL = "at least one superhero exists"
const val GET_ONE = "a requested superhero exists"

@PactTestFor(
    providerName = PROVIDER,
    port = "0" // random port
)
@ExtendWith(PactConsumerTestExt::class)
class RetrofitSuperheroClientConsumerTest {

    lateinit var requestClient: SuperheroClient

    @BeforeEach
    fun setup(mockServer: MockServer){
        MockKAnnotations.init(this)

        requestClient = Retrofit.Builder()
            .baseUrl("http://localhost:${mockServer.getPort()}")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .build().create(SuperheroClient::class.java)
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
            payload.numberType("age", 41)
//            payload.stringMatcher("adress", ".*", "ask Superman")
//            payload.eachArrayLike("hobbies", 2){
//                    arrayEntry ->
//                    arrayEntry.stringType("Hunting bad guys")
//                    arrayEntry.stringType("Hanging out with Superman")
//
//            }
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
        val result = runBlocking {
            requestClient.getSuperhero(42)
        }
        assertThat(result.name).isEqualTo("Bruce Wayne")
        assertThat(result.secretIdentity).isEqualTo("Batman")
        assertThat(result.affiliation).isEqualTo("DC")
        assertThat(result.age).isEqualTo(41)
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
                    entry.numberType("age", 25)
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
        val result = runBlocking {
            requestClient.getSuperheroes()
        }
        assertThat(result).isNotEmpty
        assertThat(result[0].name).isEqualTo("Peter Parker")
        assertThat(result[0].secretIdentity).isEqualTo("Spider-Man")
        assertThat(result[0].affiliation).isEqualTo("Marvel")
        assertThat(result[0].age).isEqualTo(25)
    }
}