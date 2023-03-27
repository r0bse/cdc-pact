package de.schroeder.provider.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.ZonedDateTime

@Entity
@Table(name = "superhero")
class Superhero(@Column(nullable = false) @NotEmpty var name: String,
                @Column(nullable = false) @NotEmpty var identity: String,
                @Column(nullable = false) @NotEmpty var affiliation: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Version
    @Column(nullable = false)
    var version: Int? = null

    @CreatedDate
    @Column(nullable = false)
    var createdAt: ZonedDateTime = ZonedDateTime.now()

    @LastModifiedDate
    @Column(nullable = false)
    var lastModified: ZonedDateTime = createdAt

}