# Uploads API

Written using: Java 8, Maven, Git, Spring Boot, Hibernate, Lombok, Swagger, H2

Tested with: Spock

## Prepare local environment

### Build application
By default application creates location for files under `C:/temp` (if you want to change it you can use `com.demo.uploads.directory` property)
```
mvn clean package
```

## Run Service
```
java -jar target/uploads-0.0.1-SNAPSHOT.jar --com.demo.uploads.directory=/some/dir
``` 
You can find swagger API under: http://localhost:8080/swagger-ui.html

### Register - example
```
curl -X POST "http://localhost:8080/register" -H  "accept: */*" -H  "Content-Type: application/json" -d "{  \"email\": \"str@i.ng\",  \"password\": \"string\"}"
```

### get my files - example
```
curl -X GET "http://localhost:8080/api/file" -H  "accept: */*" --user"str@i.ng:string"
```
