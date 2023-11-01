FROM azul/zulu-openjdk:17 AS builder
WORKDIR /app
COPY discovery-service/gradlew .
COPY discovery-service/gradle gradle
COPY discovery-service/build.gradle .
COPY discovery-service/settings.gradle .
COPY discovery-service/src src
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar

FROM azul/zulu-openjdk:17
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", \
         "/app/app.jar"]