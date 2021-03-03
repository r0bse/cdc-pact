package de.schroeder.provider.entity

import org.hibernate.type.IntegerType
import org.hibernate.type.LongType
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * an invoice as seen by Winline
 */
@Entity
@Table(name = "superhero")
class SuperheroEntity(@Column(nullable = false) @NotEmpty var name: String?,
                      @Column(nullable = false) @NotEmpty var identity: String?,
                      @Column(nullable = false) @NotEmpty var affiliation: String?) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Version
    @Column(nullable = false)
    var version: Int? = null

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