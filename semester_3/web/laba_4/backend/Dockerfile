FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle gradle.properties ./
COPY src src
RUN chmod +x ./gradlew && ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/web4-backend-1.0.0.jar app.jar
COPY jmxRemoteConfig /app/jmxRemoteConfig
RUN chmod 400 /app/jmxRemoteConfig/jmxremote.password /app/jmxRemoteConfig/jmxremote.access
EXPOSE 8080
EXPOSE 9010
ENTRYPOINT ["java", "-jar", "app.jar"]