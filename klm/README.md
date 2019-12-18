Setup
-----

### Running the application

A basic spring boot application is already provided (mainclass: `com.afkl.travel.exercise.Application`).

Either run this in gradle using `./gradlew bootRun` or using the run features from your IDE of choice.

### Embedded H2 database

During application startup an embedded h2 database is started and loaded with location and translation data (schema and example data provided below).   
You can view the database through the h2 webconsole.   

Url: [http://localhost:8080/h2-console]   
Database url: `jdbc:h2:mem:travel-api`   
Username: `sa` (no password required)   

Assignment
----------

### Implement JPA data access implementation and data layer

Implement the domain layer and included the required JPA annotations.
Implement the data access layer.

### Implement the provided `OpenApi` specification.

The case includes a specification file named `travel-api.yaml`.
This can be found in directory `src/main/openapi`.

To view the spec file in `Swagger UI` run the following gradle task:

```
./gradlew runSwaggerUI
```

Note:
The runSwaggerUI requires that `docker` is installed on your local environment!

Now visit [SwaggerUI](http://localhost:8050)

Implement the controllers required to expose this api specification.
Use the data access layer to retrieve an if necessary filter the data from the database.

Note that the api needs to be secured with basic authentication:
- username: someuser
- password: psw

### Add statistics for your backend

To get some input on our application usage we need to collect and store
some information from its traffic. Retrieve and store the require values
below.

-   Total number of requests processed
-   Total number of requests resulted in an OK response
-   Total number of requests resulted in a 4xx response
-   Total number of requests resulted in a 5xx response
-   Average response time of all requests
-   Max response time of all requests

This information must be exposed in an `/actuator/metrics` API endpoint as JSON.
This endpoint should not be accessible by normal api consumers and must be secured with:
- username: ops
- password: psw

Note:
- **Collecting metrics should not impact the user experience in any way.**
- **It is not required that these metrics survive a container restart**

### Make the application configurable

No hard coded values for things like endpoints, etc. Everything should
be configurable in some kind of configuration file.

### Create a client

Implement a client to consume the API.
Use the client to retrieve and print all airports from the USA (country code is 'US')

### Bonus points

- dockerize the application
- Provide a unique ID to each request for tracking purposes and have *every* log line include it before the message.

---

API data
--------

### Database schema

The data exposed by the API is stored in an embedded H2 database.   
Us JPA mappings to map this data to a domain model.   

The structure of the database schema is shown below:

      =================================
      | Location                      |
      |===============================|
      | id        integer (generated) |
      | code      varchar             |
      | type      varchar             |<-------|
      | longitude double              |        |
      | latitude  double              |        | parent:                           
      | parent    integer             |        | relation between locations 
      =================================        | airport -> city, city -> country   
                  |   |                        |
                  |   |                        |
                  |   --------------------------
                  |
                  |
      ==================================
      | Translation                    |
      |================================|
      | id          integer (generated)|
      | location    integer            |
      | language    varchar            |
      | name        varchar            |
      | description varchar            |
      ==================================
      
### Example data

Locations sample data:

| id | code | type | longitude | latitude | parent |
|----|------|------|-----------|----------|--------|
| 4614|NL|COUNTRY|5.45|52.3|null|
| 5366|AMS|CITY|4.78417|52.31667|4614|
| 6412|AMS|AIRPORT|4.76806|52.30833|5366|

Translations sample data:

| id   | location | language | name        | description                           |
|------|----------|----------|-------------|---------------------------------------|
| 7131 | 4614     | EN       | Netherlands | Netherlands (NL)                      |
| 7132 | 4614     | NL       | Nederland   | Nederland (NL)                        |
| 8635 | 5366     | EN       | Amsterdam   | Amsterdam (AMS)                       |
| 8636 | 5366     | NL       | Amsterdam   | Amsterdam (AMS)                       |
| 9970 | 6412     | NL       | Schiphol    | Amsterdam - Schiphol (AMS), Nederland |

---

Time limit
----------

The time limit for is 4 hours.\
Please do not take into account the time you need for the setup.

---

Summary
-------

By no means is the goal to get a solution covering all special cases in
a 100% robust way. Requiring this would be naive given the time limit!
It's important that you can explain why you chose your solution instead
of another.

At least the functionality that you deliver should be error free. What
you implement and how you do it is subject to your creativity and
ambition :-). Good luck!
