FROM openjdk:17
COPY build/libs/learning-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8091
ENTRYPOINT ["java", "-jar", "/app.jar"]