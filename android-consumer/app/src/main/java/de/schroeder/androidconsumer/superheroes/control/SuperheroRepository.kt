package de.schroeder.androidconsumer.superheroes.control

import de.schroeder.androidconsumer.superheroes.enitity.SuperheroResource
import retrofit2.http.GET
import retrofit2.http.Path

class SuperheroRepository( private val superheroClient: SuperheroClient){

    suspend fun getSuperheroes(): List<SuperheroResource>{
        return superheroClient.getSuperheroes()
    }
}

interface SuperheroClient {

    @GET("/superheroes")
    suspend fun getSuperheroes(): List<SuperheroResource>

    @GET("/superheroes/{id}")
    suspend fun getSuperhero(@Path("id") id: Int): SuperheroResource
}