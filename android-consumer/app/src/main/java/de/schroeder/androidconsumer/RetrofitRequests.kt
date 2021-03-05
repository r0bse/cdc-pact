package de.schroeder.androidconsumer

import com.google.gson.GsonBuilder
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class RetrofitRequests(
    val tableService: TableService,
    baseUrl: String
) : Callback<List<SuperheroResource>> {

    val providerClient: ProviderClient = Retrofit
        .Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .setLenient()
                    .create()
            )
        )
        .build().create(ProviderClient::class.java)

    fun getSuperheroes(){
        val call: Call<List<SuperheroResource>> = providerClient.getSuperheroes()
        call.enqueue(this)
    }

    override fun onResponse(call: Call<List<SuperheroResource>>, response: Response<List<SuperheroResource>>) {
        if (response.isSuccessful) {
            return response.body().forEach{ superhero -> tableService.addTableRow(superhero)}
        } else {
            throw RuntimeException("${response.errorBody()}")
        }
    }

    override fun onFailure(call: Call<List<SuperheroResource>>?, t: Throwable) {
        t.printStackTrace()
    }
}

interface ProviderClient {

    @GET("/superheroes")
    fun getSuperheroes(): Call<List<SuperheroResource>>

    @GET("/superheroes/{id}")
    fun getSuperhero(@Path("id") id: Int): Call<SuperheroResource>
}

data class SuperheroResource(
    val name: String,
    val secretIdentity: String,
    val affiliation: String
) : JSONObject()
