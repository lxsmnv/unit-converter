# unit-converter
## Overview
This web service will convert measurement units to their SI counterparts. 

The following end point is exposed:
```
METHOD: GET
PATH: /units/si
PARAMS: units - A unit string
RETURNS: conversion - A conversion object
```

##Implementation
The following libraries were used in the implementation:
 - Http4s and Cats Effect HTTP/Web server
 - Circe json

## Build

```sbt package```

Build docker image:
```sbt docker:publishLocal```

## Run tests
```sbt test```

## Run
```sbt run```

Run with docker:

```docker run --rm --name unit-converter -i unit-converter:0.1```

Example request:
http://127.0.0.1:8080/units/si?units=(degree/minute)