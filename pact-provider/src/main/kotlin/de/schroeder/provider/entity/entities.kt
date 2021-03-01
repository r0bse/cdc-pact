package de.schroeder.provider.entity

import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import javax.persistence.Table
import javax.persistence.Version
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * an invoice as seen by Winline
 */
@Entity
@Table(name = "superhero")
class Superhero(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long,
                    @NotEmpty var name: String?,
                    @NotEmpty var identity: String?,
                    @NotEmpty var affiliation: String?) {

    lateinit var createdDate: ZonedDateTime
    lateinit var lastModifiedDate: ZonedDateTime

    @PrePersist
    fun preCreate() {
        val now = ZonedDateTime.now(ZoneId.of("UTC"))
        createdDate = now
        lastModifiedDate = now
    }

    @PreUpdate
    fun preUpdate() {
        lastModifiedDate = ZonedDateTime.now(ZoneId.of("UTC"))
    }
}