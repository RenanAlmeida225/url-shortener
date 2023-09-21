# Url shortener

It is an API for shortening urls, with a maximum time limit of 30 days, if the url reaches the day limit it is automatically deleted.

## Routes

| Methods | Urls             | Actions                  |
| ------- | ---------------- | ------------------------ |
| POST    | /url             | create an short url      |
| GET     | /url/unshortener | unshortener an short url |
| POST    | /url/report      | report an url malicious  |
| GET     | /{short_url}     | redirect to original url |

## Dependencies

There are a number of third-party dependencies used in the project. Browse the Maven pom.xml file for details of libraries and versions used.

## Building the project

You will need:

- Java JDK 17 or higher
- Maven 3.1.1 or higher
- Git
- Postgres

Clone the project and use Maven to build the server

    $ mvn clean install

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the main method in the de.codecentric.springbootsample.Application class from your IDE.

Alternatively you can use the Spring Boot Maven plugin like so:

    mvn spring-boot:run
