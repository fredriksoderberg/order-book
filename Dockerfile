FROM openjdk:17-alpine

COPY target/*.jar order-book-service.jar

ENTRYPOINT ["java", "-jar",  "order-book-service.jar"]
