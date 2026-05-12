FROM openjdk:23-ea-23-jdk-slim

WORKDIR /app

COPY bot-gateway-service/target/bot-gateway-service-0.0.1-SNAPSHOT.jar bot-gateway-service.jar

CMD ["java", "-jar", "bot-gateway-service.jar"]

EXPOSE 8080
