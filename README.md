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
* start the docker-compose file
* change the API or an API-Call
**  either a call to one by maniupulating the requested Object or the URL
** or by changing the API within the provider (RequestObject, Headers, URL)
* run `./gradlew test` in provider-service
** should fail with complaining about not missing the API contract
