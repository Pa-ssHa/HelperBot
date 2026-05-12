FROM openjdk:23-ea-23-jdk-slim

WORKDIR /app

COPY bot-vacancy-service/target/bot-vacancy-service-0.0.1-SNAPSHOT.jar bot-vacancy-service.jar

CMD ["java", "-jar", "bot-vacancy-service.jar"]

EXPOSE 8080