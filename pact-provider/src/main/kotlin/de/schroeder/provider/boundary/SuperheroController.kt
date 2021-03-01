package de.schroeder.provider.boundary

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path=["/superheroes"])
class SuperheroController(){

    @GetMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAll() : ResponseEntity<List<SuperheroResource>>{
        val heroes = listOf(SuperheroResource("Batman", "DC", "Bruce Wayne"),
            SuperheroResource("Peter Porker", "Marvel", "Spider-Ham"))
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
            .body(heroes)
    }

    @GetMapping("/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getOne(@PathVariable id: Long): ResponseEntity<SuperheroResource>{
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
            .body(SuperheroResource("Peter Porker", "Marvel", "Spider-Ham"))
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody createRequest: SuperheroResource): ResponseEntity<SuperheroResource>{
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build()
    }
}

data class SuperheroResource(val name:String,
                            val affiliation: String,
                            val secretIdentity: String)
