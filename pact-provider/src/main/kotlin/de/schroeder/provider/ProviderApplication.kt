package de.schroeder.provider

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import com.zaxxer.hikari.HikariDataSource

import org.springframework.beans.BeansException

import org.springframework.beans.factory.config.BeanPostProcessor




@EnableJpaRepositories
@SpringBootApplication
class ProviderApplication{
}

fun main(args: Array<String>) {
    runApplication<ProviderApplication>(*args)
}
