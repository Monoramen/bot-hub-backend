# Stage 1: Build the application
FROM openjdk:23-jdk-slim AS builder
WORKDIR /trm-251-firing-manager
COPY . .
# Add this line to give the gradlew script execute permissions
RUN chmod +x gradlew
RUN ./gradlew clean
RUN ./gradlew build --no-daemon

# Stage 2: Create the final production image
FROM openjdk:23-jdk-slim
WORKDIR /trm-251-firing-manager
COPY --from=builder /trm-251-firing-manager/build/libs/bot-hub-backend.jar backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]