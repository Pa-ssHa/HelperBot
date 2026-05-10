FROM openjdk:23-ea-23-jdk-slim

WORKDIR /app

COPY ai-resume-service/target/ai-resume-0.0.1-SNAPSHOT.jar ai-resume-service.jar

CMD ["java", "-jar", "ai-resume-service.jar"]

EXPOSE 8081