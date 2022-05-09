package de.schroeder.provider.control

import de.schroeder.provider.entity.Superhero
import de.schroeder.provider.entity.SuperheroResource
import de.schroeder.provider.entity.SuperheroResponse
import java.time.ZonedDateTime

fun Superhero.toResponse() : SuperheroResponse{

    return SuperheroResponse(this.name,
                            this.affiliation,
                            this.identity,
                            ZonedDateTime.now().year - this.birthday.year
    )
}

fun SuperheroResource.toEntity() : Superhero{

    return Superhero(name = this.name,
                     affiliation = this.affiliation,
                     identity = this.secretIdentity,
                     birthday = this.birthday)
}