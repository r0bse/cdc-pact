# Pact-Provider

## What this Project is about
Example Project to showcase a consumer driven approach to API developement.

# Start
Ensure that the database `superheroes`does exist, 
then run: `./gradlew bootRun`

# Build

`./gradlew build`

## Test

*To run the consumer tests you need a local PactBroker running.* 
Run: `./gradlew test`.

## verify Contracts
The verification result will be sent to the pactbroker after each test run.

#TODOs
* implement verification against tags (or latest if it is empty)
