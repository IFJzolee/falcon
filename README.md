# Falcon.io assessment

### How to run it locally
Start the local infrastructure.
```
cd infra/local
docker-compose up
```

Once it's up run the application with `local` spring profile in your IDE or in your command line.

```
mvn clean package -DskipDockerBuild -DskipTests
java -jar target/assessment-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

The app uses port `8080`.

### How to build the project

Start the local infrastructure.
```
cd infra/local
docker-compose up
```

Then run the following maven command. It will run the unit and integration tests also build a docker image.

`mvn clean verify`

If you want to skip the docker build use maven with the `-DskipDockerBuild` option.

### API

Rest API documentation is accessible at path `/swagger-ui/`
In local it's `http://localhost:8080/swagger-ui/`.
### Main Page

The service also provides an HTML main page at `/` where real time message flow is visible.
In local it's `http://localhost:8080`.
