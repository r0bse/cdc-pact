package de.schroeder.consumer.entity

data class CreateRequest(val name: String,
                         val affiliation: String,
                         val identity: String)