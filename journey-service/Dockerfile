FROM openjdk:17.0.1-jdk-slim AS builder
WORKDIR /app
ARG JAR_FILE=build/libs/*jar
COPY ${JAR_FILE} app.jar
EXPOSE 8600
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
