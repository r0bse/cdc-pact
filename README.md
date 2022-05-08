# Consumer Driven Contracting (with Pact)

Example project to showcase a workflow of Consumer Driven Contract Testing with Pact.

Basic premise is a Backend-Service (Provider) which spoiles the identities of superheroes.
This info is consumed by a Consumer-Service and tested within the build by downloading Pacts from a PactBroker.

# Goals of this project
Providing an example how to manage several (different) consumers for one Backend API.
All consumerd and provided APIs will be tested by using Pact, a Pactbroker and canIdeploy.

# Before you start

## Prerequisities
The docker-compose in the root directory should be run before trying to test the cdc workflow or running the provider-service.
It contains a PactBroker and a Postgres Database.

## Provider-Service
Needs a Postgres DB running in background (with provider-database). Provided in docker-compose.yml

More description in subfolder.

## Consumer-Servive
Can startup on it's own
Needs Provider-Service for full functionality.

More description in subfolder.

## Android-Consumer
Can start on its own. 
Needs Provider-Service for full functionality.

More description in subfolder.

# How do I start?

## Using the Example
* start the docker-compose file with `docker-compose up`
* create a database named `superheroes` on the started postgres container
* start the superhero-provider-service with an IDE of your choice
* start the Android-App or the Consumer-Service with an IDE of your Choice


## change the API 
* change the API or an API-Call within one of the consumers
    * either the requestclient by manipulating the requested Object or the URL
    * or by changing the API within the provider (RequestObject, Headers, URL, etc.)
    * basically change anything (payload or request)
    * run the tests by running the gradle test goal
    * run `./gradlew test pactPublish`
    * this will upload the changed pact
* run `./gradlew test` in provider-service
  * will fail and complain about not passing the verification of a pact
    
# TODOs
* Too much too write about