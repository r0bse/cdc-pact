# Pact-Consumer

## What this Project is about
Example Project to showcase a consumer driven approach to API developement.

## What this service represents
Represents an Consumer for administering Superheroes in Provider-Service.

# Start the programm
`./gradlew bootRun`

# Build

`./gradlew build`

## Test

*To run the consumer tests you need a local PactBroker running.* 
Run: `./gradlew test`.

## publish Contracts
Run: `./gradlew pactPublish -Dpact.provider.tag=foo,bar -DpactBrokerUrl=localhost -DpactBrokerUsername=admin -DpactBrokerPassword=password`.

#TODOs
* implement canIdeploy example
* bei zuviel Langeweile ein simples Thymeleaf Frontend um eine Browser AdminOberfläche zu haben