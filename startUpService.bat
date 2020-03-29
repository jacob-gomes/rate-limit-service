
# Build service 
call mvn clean install -DskipTests

# run the service
java -jar target/rate-limit-service-0.0.1-SNAPSHOT.jar --spring.config.location=src/main/resources/application.properties
