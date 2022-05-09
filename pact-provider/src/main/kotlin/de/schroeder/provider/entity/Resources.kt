package de.schroeder.provider.entity

import java.time.ZonedDateTime

data class SuperheroResource(val name:String,
                             val affiliation: String,
                             val secretIdentity: String,
                             var birthday: ZonedDateTime)

data class SuperheroResponse(val name:String,
                             val affiliation: String,
                             val secretIdentity: String,
                             var age: Int)