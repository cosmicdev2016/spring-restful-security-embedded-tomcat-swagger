# Spring Restful Security Embedded Tomcat Swagger App Without Spring Boot

## Technology/Tools Used

1. Java 8
2. Spring 4.3.17.RELEASE
3. Embedded Tomcat 7
4. Swagger 2
5. slf4j + logback
6. Maven 3.5.x

## Setup

1. Download `JDK 8` (ignore, if already present).
2. Download `apache-maven-3.5.4` (ignore, if already present). 
3. Change log file location: 
	1. Edit `logback.xml` and change the path for `LOG_HOME` property.
4. Command to package and build war file: `mvn clean compile package`

## Run Book

**1. Use Tomcat server**
1. OPTIONAL: WAR file is added to `target` folder in case of any issues with Maven build.
2. Copy the war file to `TOMCAT_HOME/webapps`.
3. Start tomcat by running: `startup` (make sure `TOMCAT_HOME/bin` is in your environmnet PATH variable)

**2. Use Embedded Tomcat server** (optional)
1. Start Embedded Tomcat: `mvn tomcat7:run`

**3. Accessing the application**
1. Two ways in which you can access the application:
	1. **Using Postman:** Open the Postman app. Import `mytest.postman_collection.json` file into Postman. Use `No Auth` and `Basic Auth` in Postman and try to access the REST API.
	2. **Using Swagger UI:** Open link [Swagger UI](http://localhost:8080/transaction/swagger-ui.html) after tomcat is up and running.
		1. Click `List Operations` link to access all the API links.
		2. Input parameters to the API as appropriate and click `Try it out` button.
		3. Enter username and password for `Basic Auth' when browser prompts.
2. Credentials for `Basic Auth` are: 
	1. **User id:** `admin`
	2. **Password:** `admin123`
3. Log location is `C:\logs` and log filename is `transaction-debug.log`
