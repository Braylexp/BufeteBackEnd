FROM amazoncorretto:21-alpine-jdk

COPY target/backend-0.0.1-SNAPSHOT.jar /backend-app.jar

EXPOSE 8081
ENTRYPOINT [ "java", "-jar", "/backend-app.jar" ]