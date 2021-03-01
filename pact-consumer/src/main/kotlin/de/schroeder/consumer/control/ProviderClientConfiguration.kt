package de.schroeder.consumer.control

import feign.Logger
import feign.RequestInterceptor
import feign.RequestTemplate
import feign.Util
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean

class ProviderClientConfiguration {

    /**
     * add a custom header value to every request
     */
    @Bean
    fun customHeaderRequestInterceptor(): CustomHeaderRequestInterceptor {
        return CustomHeaderRequestInterceptor("foo", "bar")
    }

    @Bean
    fun feignLoggerLevel() : Logger.Level{
        return Logger.Level.FULL;
    }
}


class CustomHeaderRequestInterceptor(val headerName: String,
                                     val headerValue: String) : RequestInterceptor {

    override fun apply(template: RequestTemplate) {
        template.header(headerName, listOf(headerValue))
    }
}