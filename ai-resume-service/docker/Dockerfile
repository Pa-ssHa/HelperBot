FROM maven:3.9.9-eclipse-temurin-23

WORKDIR /app

#COPY ai-resume-service/target/ai-resume-service-0.0.1-SNAPSHOT.jar ai-resume-service.jar
#
#CMD ["java", "-jar", "ai-resume-service.jar"]

CMD ["mvn", "spring-boot:run"]

EXPOSE 8081