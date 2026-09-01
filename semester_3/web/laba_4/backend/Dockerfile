FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN gradle myBuild -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/web4-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]