FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

ARG APP_NAME=messaging-service
ENV APP_NAME = ${APP_NAME}

COPY target/messaging-service.jar messaging-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "messaging-service.jar"]