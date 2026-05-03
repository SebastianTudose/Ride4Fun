# Etapa 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean install -DskipTests
RUN mvn clean package -pl frontend -am -Pproduction -DskipTests

# Etapa 2: Run
FROM eclipse-temurin:21-jre
COPY --from=build /frontend/target/*.war /app.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.war"]