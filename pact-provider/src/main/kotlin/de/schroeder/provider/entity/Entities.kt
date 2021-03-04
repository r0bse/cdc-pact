package de.schroeder.provider.entity

import org.hibernate.type.IntegerType
import org.hibernate.type.LongType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
@Table(name = "superhero")
class Superhero(@Column(nullable = false) @NotEmpty var name: String?,
                @Column(nullable = false) @NotEmpty var identity: String?,
                @Column(nullable = false) @NotEmpty var affiliation: String?) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Version
    @Column(nullable = false)
    var version: Int? = null

    @CreatedDate
    @Column(nullable = false)
    lateinit var createdAt: ZonedDateTime

    @LastModifiedDate
    @Column(nullable = false)
    lateinit var lastModified: ZonedDateTime
}