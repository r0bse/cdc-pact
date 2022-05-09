package de.schroeder.provider.control

import de.schroeder.provider.entity.Superhero
import de.schroeder.provider.entity.SuperheroResource

fun Superhero.toResource() : SuperheroResource{

    return SuperheroResource(this.name!!,
                            this.affiliation!!,
                            this.identity!!)
}

fun SuperheroResource.toEntity() : Superhero{

    val hero = Superhero(name = this.name,
                         affiliation = this.affiliation,
                         identity = this.secretIdentity)

    return hero
}