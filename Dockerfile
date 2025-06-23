# Build stage
FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Check if the WAR file exists after build
RUN ls -la /app/target

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar /app/drcomputer.jar

# Check if the WAR file exists in the final stage
RUN ls -la /app

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/drcomputer.jar"]