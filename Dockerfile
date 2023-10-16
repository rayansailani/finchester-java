# Use a multi-stage build for Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Use a smaller base image for the final application
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/target/finchester-java-0.0.1-SNAPSHOT.jar finchester-java.jar
CMD ["java", "-jar", "finchester-java.jar"]
