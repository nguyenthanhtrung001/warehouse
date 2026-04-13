# ========================
# 1. Build stage
# ========================
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Cache dependency
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source
COPY src ./src

# Build
RUN mvn clean package -Dmaven.test.skip=true

# ========================
# 2. Run stage
# ========================
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy JAR
COPY --from=build /app/target/*.jar app.jar

# Port
EXPOSE 8888

# Run
ENTRYPOINT ["java", "-jar", "app.jar"]