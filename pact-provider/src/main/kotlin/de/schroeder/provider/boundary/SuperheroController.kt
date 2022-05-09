package de.schroeder.provider.boundary

import de.schroeder.provider.control.SuperheroService
import de.schroeder.provider.entity.SuperheroResource
import de.schroeder.provider.entity.SuperheroResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder

@RestController
@RequestMapping(path=["/superheroes"])
class SuperheroController(val superheroService: SuperheroService){

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAll() : ResponseEntity<List<SuperheroResponse>>{
        val heroes = superheroService.findAll()
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
            .body(heroes)
    }

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getOne(@PathVariable id: Long): ResponseEntity<SuperheroResponse>{
        val hero = superheroService.findOne(id)
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
            .body(hero)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody createRequest: SuperheroResource): ResponseEntity<SuperheroResponse>{
        val hero = superheroService.create(createRequest)
        val uri = MvcUriComponentsBuilder.fromController(javaClass)
            .path("/superheroes/{id}")
            .buildAndExpand(hero.id)
            .toUri()
        return ResponseEntity.created(uri).contentType(MediaType.APPLICATION_JSON).build()
    }
}