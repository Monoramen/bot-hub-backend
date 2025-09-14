# Stage 1: Build the application
FROM openjdk:23-jdk-slim AS builder

WORKDIR /app

COPY . .

RUN chmod +x gradlew

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew build --no-daemon --stacktrace

# Stage 2: Create the final production image
FROM openjdk:23-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/bot-hub-backend.jar backend.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "backend.jar"]