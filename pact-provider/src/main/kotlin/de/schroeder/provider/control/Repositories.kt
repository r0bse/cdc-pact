package de.schroeder.provider.control

import de.schroeder.provider.entity.SuperheroEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SuperheroRepository : JpaRepository<SuperheroEntity, Long> {}