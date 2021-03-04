package de.schroeder.provider.control

import de.schroeder.provider.entity.Superhero
import de.schroeder.provider.entity.SuperheroResource
import org.springframework.stereotype.Service

@Service
class SuperheroService(val superheroRepository: SuperheroRepository) {

    fun findOne(id: Long): SuperheroResource {
        return superheroRepository.findById(id).get().toResource()
    }
    fun findAll(): List<SuperheroResource> {
        return superheroRepository.findAll().map { it.toResource() }
    }
    fun create(hero: SuperheroResource): Superhero {
        return superheroRepository.save(hero.toEntity())
    }
}