FROM openjdk:11.0.9-jdk

ARG JAR_FILE

RUN mkdir /application
COPY $JAR_FILE /application/app.jar

EXPOSE 8080

WORKDIR /application
ENTRYPOINT ["java", "-jar", "-server", "app.jar"]
