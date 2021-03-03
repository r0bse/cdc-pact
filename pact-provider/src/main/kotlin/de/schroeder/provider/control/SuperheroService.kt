package de.schroeder.provider.control

import de.schroeder.provider.entity.SuperheroEntity
import de.schroeder.provider.entity.SuperheroResource
import org.springframework.stereotype.Service

@Service
class SuperheroService(val superheroRepository: SuperheroRepository) {

    fun findOne(id: Long): SuperheroEntity {
        return superheroRepository.findById(id).get()
    }
    fun findAll(): List<SuperheroEntity> {
        return superheroRepository.findAll()
    }
    fun create(hero: SuperheroResource): SuperheroEntity {
        return superheroRepository.save(hero.toEntity())
    }
}