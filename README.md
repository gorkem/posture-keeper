# Posture Keeper

Posture keeper is a REST-based web service that allows clients to store and retrieve SAST analysis data for a specific git repository and commit SHA.  The service is implemented using Quarkus and uses MongoDB as its underlying data store.

## Development

### Running the Posture Keeper in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```
### Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

### Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/posture-keeper-1.0.0-SNAPSHOT-runner`

