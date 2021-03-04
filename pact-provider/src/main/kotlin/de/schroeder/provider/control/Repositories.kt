package de.schroeder.provider.control

import de.schroeder.provider.entity.Superhero
import org.springframework.data.jpa.repository.JpaRepository

interface SuperheroRepository : JpaRepository<Superhero, Long> {}