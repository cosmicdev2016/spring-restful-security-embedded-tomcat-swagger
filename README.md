# Spring Restful Security Embedded Tomcat Swagger App Without Spring Boot

## Technology/Tools Used

1. Java 8
2. Spring 4.3.17.RELEASE
3. Embedded Tomcat 7
4. Swagger 2

## Setup

1. Download `apache-maven-3.5.4` (if you don't have already). 
2. Command to package and build war file: `mvn clean compile package`

## Run Book

1. Start Embedded Tomcat: `mvn tomcat7:run`
2. Two ways in which you can access the application:
	1. **Using Postman:** Open the Postman app. Import `mytest.postman_collection.json` file into Postman. Use `No Auth` and `Basic Auth` in Postman and try to access the REST API.
	2. **Using Swagger UI:** Open link [Swagger UI](http://localhost:8080/transaction/swagger-ui.html) after tomcat is up and running.
3. Credentials for `Basic Auth` are: 
	1. **User id:** `admin`
	2. **Password:** `admin123`
