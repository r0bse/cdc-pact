package de.schroeder.provider.control

import de.schroeder.provider.entity.Superhero
import de.schroeder.provider.entity.SuperheroResource
import de.schroeder.provider.entity.SuperheroResponse
import org.springframework.stereotype.Service

@Service
class SuperheroService(val superheroRepository: SuperheroRepository) {

    fun findOne(id: Long): SuperheroResponse {
        return superheroRepository.findById(id).get().toResponse()
    }
    fun findAll(): List<SuperheroResponse> {
        return superheroRepository.findAll().map { it.toResponse() }
    }
    fun create(hero: SuperheroResource): Superhero {
        return superheroRepository.save(hero.toEntity())
    }
}