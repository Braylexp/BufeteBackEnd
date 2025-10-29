FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clen install

FROM eclipse-eclipse-temurin:17-jdk
COPY --from=build /target/backend-0.0.1-SNAPSHOT.jar backend-app.jar
EXPOSE 8081
ENTRYPOINT [ "java", "-jar", "backend-app.jar" ]