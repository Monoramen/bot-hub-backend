FROM gradle:8.5-jdk17 AS builder
COPY --chown=gradle:gradle . /home/gradle/project
WORKDIR /home/gradle/project
RUN gradle build --no-daemon

FROM openjdk:23-jdk-slim
WORKDIR /trm-251-firing-manager
COPY /build/libs/bot-hub-backend.jar backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]