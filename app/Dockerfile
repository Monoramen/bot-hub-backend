FROM openjdk:23-jdk-slim
WORKDIR /app
COPY build/libs/personal-bot-hub.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]