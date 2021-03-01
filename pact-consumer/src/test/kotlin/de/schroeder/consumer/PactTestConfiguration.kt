//package de.schroeder.consumer
//
//import de.schroeder.consumer.control.ProviderClient
//import feign.Client
//import feign.Feign
//import org.springframework.beans.factory.annotation.Qualifier
//import org.springframework.boot.test.context.TestConfiguration
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Import
//
//@TestConfiguration
//@Import(ConsumerApplication::class)
//class PactTestConfiguration {
//
//    /**
//     * necessary to be able to set the url dynamically in pact-consumer-tests
//     */
//    @Bean
//    fun feignBuilder(): Feign.Builder {
//        return Feign.builder()
//    }
//}