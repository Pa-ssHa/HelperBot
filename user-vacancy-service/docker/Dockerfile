FROM maven:3.9.9-eclipse-temurin-23

WORKDIR /app

#COPY user-vacancy-service/target/user-vacancy-service-0.0.1-SNAPSHOT.jar user-vacancy-service.jar
#
#CMD ["java", "-jar", "user-vacancy-service.jar"]

CMD ["mvn", "spring-boot:run"]

EXPOSE 8082
