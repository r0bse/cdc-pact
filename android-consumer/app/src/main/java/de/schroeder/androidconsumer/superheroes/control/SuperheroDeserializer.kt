package de.schroeder.androidconsumer.superheroes.control

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import de.schroeder.androidconsumer.superheroes.enitity.SuperheroResource
import java.lang.reflect.Type

class SuperheroesDeserializer : JsonDeserializer<List<SuperheroResource>> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): List<SuperheroResource> {

        return json.asJsonArray.map{
            context.deserialize<SuperheroResource>(it, SuperheroResource::class.java)
        }.toList()
    }
}

class SuperheroDeserializer : JsonDeserializer<SuperheroResource> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): SuperheroResource {

        val heroJson = json.asJsonObject

        val name = heroJson.get("name").asString
        val secretIdentity = heroJson.get("secretIdentity").asString
        val affiliation = heroJson.get("affiliation").asString

        return SuperheroResource(name, secretIdentity, affiliation)
    }
}