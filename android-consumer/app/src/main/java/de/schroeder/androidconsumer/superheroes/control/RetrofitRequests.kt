package de.schroeder.androidconsumer

import de.schroeder.androidconsumer.superheroes.control.SuperheroTableManager
import de.schroeder.androidconsumer.superheroes.enitity.SuperheroResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

class RetrofitRequests (val superheroTableManager: SuperheroTableManager,
                        val retrofitSuperheroClient: RetrofitSuperheroClient) : Callback<List<SuperheroResource>> {

    fun getSuperheroes(){
        val call: Call<List<SuperheroResource>> = retrofitSuperheroClient.getSuperheroes()
        call.enqueue(this)
    }

    override fun onResponse(call: Call<List<SuperheroResource>>, response: Response<List<SuperheroResource>>) {
        if (response.isSuccessful) {
            superheroTableManager.addSuperheroToTable(response.body())
        } else {
            throw RuntimeException("${response.errorBody()}")
        }
    }

    override fun onFailure(call: Call<List<SuperheroResource>>?, t: Throwable) {
        throw RuntimeException("${t.message}")
    }
}

interface RetrofitSuperheroClient {

    @GET("/superheroes")
    fun getSuperheroes(): Call<List<SuperheroResource>>

    @GET("/superheroes/{id}")
    fun getSuperhero(@Path("id") id: Int): Call<SuperheroResource>
}