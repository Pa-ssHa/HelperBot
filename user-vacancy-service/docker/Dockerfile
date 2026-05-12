FROM openjdk:23-ea-23-jdk-slim

WORKDIR /app

COPY user-vacancy-service/target/user-vacancy-service-0.0.1-SNAPSHOT.jar user-vacancy-service.jar

CMD ["java", "-jar", "user-vacancy-service.jar"]

EXPOSE 8082
