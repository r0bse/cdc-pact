package de.schroeder.consumer.control

import de.schroeder.consumer.entity.CreateRequest
import de.schroeder.consumer.entity.GetResource
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(value = "provider-service", url="\${provider-service.url}:\${provider-service.port}", configuration = [ProviderClientConfiguration::class])
interface ProviderClient {

    @GetMapping(path = ["/superheroes"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getAll(): ResponseEntity<List<GetResource>>

    @GetMapping(path = ["/superheroes/{id}"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getOne(@PathVariable("id") id: Long): ResponseEntity<GetResource>

    @PostMapping(path = ["/superheroes"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody request: CreateRequest): ResponseEntity<Void>
}