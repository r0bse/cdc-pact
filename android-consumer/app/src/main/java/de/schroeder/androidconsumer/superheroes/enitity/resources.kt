package de.schroeder.androidconsumer.superheroes.enitity

import org.json.JSONObject


data class SuperheroResource(val name: String,
                             val secretIdentity: String,
                             val affiliation: String) : JSONObject()