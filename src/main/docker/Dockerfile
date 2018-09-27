FROM openjdk:8-slim

ARG appId

ADD maven/$appId $APP_HOME/app.jar

ENV spring.profiles.active=docker

CMD ["java", "-jar", "/app.jar"]