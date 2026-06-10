FROM gradle:8.12-jdk17-alpine AS build
WORKDIR /app
COPY build.gradle settings.gradle gradle.properties ./
COPY src ./src
RUN gradle myBuild -x functionalTest --no-daemon
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/lab4-backend.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]