# Build stge
# Use an official Gradle image to build the application
FROM gradle:8.10.0-jdk8 AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY src ./src

# Build the application
RUN gradle shadowJar --no-daemon

# Run stage
FROM openjdk:8u151-jdk-alpine3.7

# Install Bash
RUN apk add --no-cache bash libc6-compat coreutils

# Copy resources
WORKDIR /app
COPY wait-for-it.sh wait-for-it.sh
COPY --from=builder /app/build/libs/flink-kafka2postgres-1.0-SNAPSHOT-jar-with-dependencies.jar flink-processor.jar

# Wait for Zookeeper and Kafka to be available and run application
CMD ./wait-for-it.sh -s -t 30 $ZOOKEEPER_SERVER -- ./wait-for-it.sh -s -t 30 $KAFKA_SERVER -- java -Xmx512m -jar flink-processor.jar
