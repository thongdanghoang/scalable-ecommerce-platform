FROM openjdk:11 AS runstage
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]