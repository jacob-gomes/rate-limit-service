# Installing & Running Code

## Prerequisites:

#### Ensure jdk 1.8 and maven 3.6.1 or above is set in your class path
#### Unzip the project to your local machine 


## Running the code on Windows(recomended):

#### Open command prompt navigate to rate-limit-service
#### > mvn clean install -DskipTests

#### Run the integration test (optional)
#### > mvn integration-test

#### Run the rest server:
#### > java -jar target/rate-limit-service-0.0.1-SNAPSHOT.jar --spring.config.location=src/main/resources/application.properties

# Task side notes
## Enhancement can be done:
#### Can enhance the service to use mongoDB as database
