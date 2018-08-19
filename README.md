# Spring Restful Security Embedded Tomcat Swagger App Without Spring Boot

## Technology/Tools Used

1. Java 8
2. Spring 4.3.17.RELEASE
3. Embedded Tomcat 7
4. Swagger 2

## Setup

1. Use `apache-maven-3.5.4` to package the web application. Use this command to package and build war file: `mvn clean compile package`

## Run Book

1. Start Embedded Tomcat: `mvn tomcat7:run`
2. Access the application in two ways:
	1. Using Postman: Open the Postman app. Import `mytest.postman_collection.json` file into Postman. Use `No Auth` and `Basic Auth` in Postman and try to access the REST API.
	2. Using Swagger UI: Open link [Swagger UI](http://localhost:8080/transaction/swagger-ui.html) after tomcat is up and running.
3. Credentials for `Basic Auth` are: **User id:** `admin` and **Password:** `admin123`
