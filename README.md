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

#### Alternatively, for build and deployment of code use startUpService.bat

## Building the code on Eclipse:

#### Unzip the project in your Workspace folder for simplicity
#### Open Eclipse (Oxygen preferred)
#### Right click on the Project Explorer and click Import -> Import
#### Choose Maven -> Existing Maven Project -> Next
#### In the new dialog box Browse to 'rate-limit-service-master' directory for the field 'Root Directory'
#### Go To Toolbar Click Run -> Run Configurations... -> Double Click 'Maven Build' 
#### A new dialog box opens -> Fill the following attribute
#### Name: <PROPER_NAME> , Base Directory: ${workspace_loc:/rate-limit-service-master}, Goals: clean install -DskipTests
#### Click Apply -> Click Run
#### Refresh the project in Eclipse, do Maven update if needed



## Running the code on Eclipse:

#### After building the code on Eclipse -> Go To Toolbar Click Run -> Run Configurations... -> Java Application
#### A new dialog box opens -> Fill the following attribute
#### Name: <PROPER_NAME>, Project: rate-limit-service-master, Main class: com.blue.optima.assignment.App
#### Click on Argument and fill 'Program Arguments' with --spring.config.location=src/main/resources/application.properties
#### Click apply -> Click Run



# Task side notes
## Enhancement can be done:
#### Can enhance the service to use mongoDB as database
