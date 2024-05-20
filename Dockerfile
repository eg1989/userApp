FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/UserApp-*-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]
