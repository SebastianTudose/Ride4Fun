# Etapa 1: Compilare (Build)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiem toate fișierele proiectului
COPY . .

# Intrăm în folderul backend și îl instalăm
WORKDIR /app/backend
RUN mvn clean install -DskipTests

# Intrăm în folderul frontend și îl compilăm pentru producție
WORKDIR /app/frontend
RUN mvn clean package -Pproduction -DskipTests

# Etapa 2: Rulare (Run)
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiem doar fișierul .war rezultat din etapa de build
COPY --from=build /app/frontend/target/*.war app.war

# Portul pentru Render
EXPOSE 8080

# Comanda de pornire (folosește variabila pentru baza de date din Render)
ENTRYPOINT ["sh", "-c", "java -Djakarta.persistence.jdbc.url=${JDBC_DATABASE_URL} -jar app.war"]