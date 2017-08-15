# Feed Screener

This monitors an HTTP endpoint, parses the XML and saves it into a database.
This uses Spring Batch, Scheduling, JAXB, H2 database.

## Prerequisites
- Java 8 or later
- Gradle 4.0.1 (to build)
- Set the environment variable `H2_DB_LOCATION` to specify the location of the
H2 database file. If the file doesn't exist, it will be created.

### Windows
`set H2_DB_LOCATION=~/feedscreener.db`

### GNU/Linux
`H2_DB_LOCATION=~/feedscreener.db`

## Compile
[![Build Status](https://travis-ci.org/pybeaudouin/feedscreener.svg?branch=master)](https://travis-ci.org/pybeaudouin/feedscreener)

`gradle build`

This will create an executable jar that runs a web server.
The jar will be created in the build/lib directory.

## Run

`java -jar build/libs/feedscreener-x.y.z.jar`
