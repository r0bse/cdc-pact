package de.schroeder.consumer.boundary

import de.schroeder.consumer.control.ProviderClient
import de.schroeder.consumer.entity.CreateRequest
import de.schroeder.consumer.entity.GetResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path=["/"],produces = [MediaType.APPLICATION_JSON_VALUE])
class ConsumerController(val providerClient: ProviderClient){

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll() : ResponseEntity<List<GetResource>>{
        return providerClient.getAll()
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getOne(@PathVariable id: Long): ResponseEntity<GetResource>{
        return providerClient.getOne(id)
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody createRequest: CreateRequest): ResponseEntity<Void>{
        return providerClient.create(createRequest)
    }
}