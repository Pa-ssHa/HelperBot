FROM maven:3.9.9-eclipse-temurin-23

WORKDIR /app

#COPY bot-gateway-service/target/bot-gateway-service-0.0.1-SNAPSHOT.jar bot-gateway-service.jar
#
#CMD ["java", "-jar", "bot-gateway-service.jar"]

CMD ["mvn", "spring-boot:run"]

EXPOSE 8080
