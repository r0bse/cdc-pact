# cdc-pact

Example project to showcase a workflow of Consumer Driven Contract Testing with Pact.

Basic premise is a Backend-Service (Provider) which spoiles the identities of superheroes.
This info is consumed by a Consumer-Service and tested within the build by downloading Pacts from a PactBroker.

# Before you start

## Prerequisities
The docker-compose in the root directory should be run before trying to test the cdc workflow or running the provider-service.
It contains a PactBroker and a Postgres Database.

## Provider-Service
Needs Postgres DB running in background (with provider-database)

## Consumer-Servive
Can startup on it's own
Needs Provider-Service for full functionality.

## Android-Consumer
Can start on its own. 
Needs Provider-Service for full functionality.
