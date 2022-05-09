package de.schroeder.consumer.entity

import java.time.ZonedDateTime

data class CreateRequest(val name: String,
                         val affiliation: String,
                         val secretIdentity: String,
                         val birthday: ZonedDateTime)