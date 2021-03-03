package de.schroeder.provider.control

import de.schroeder.provider.entity.SuperheroEntity
import de.schroeder.provider.entity.SuperheroResource

fun SuperheroEntity.toResource() : SuperheroResource{

    return SuperheroResource(this.name!!,
                            this.affiliation!!,
                            this.identity!!)
}

fun SuperheroResource.toEntity() : SuperheroEntity{

    val hero = SuperheroEntity( name = this.name,
                                affiliation = this.affiliation,
                                identity = this.secretIdentity)

    return hero
}