package de.schroeder.consumer.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class SuperheroResource(val name: String,
                             val affiliation: String,
                             @JsonProperty("secretIdentity")
                             val identity: String)