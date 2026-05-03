# Compilare
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

# Rulare (Run) pe un server Jetty
FROM jetty:11.0.21-jdk21

# Copiem arhiva compilată și o redenumim ROOT.war pentru a rula direct pe adresa principală
COPY --from=build /app/frontend/target/*.war /var/lib/jetty/webapps/ROOT.war

# Portul standard
EXPOSE 8080

# Comanda de pornire a serverului Jetty, injectând variabila bazei de date din Render
CMD ["sh", "-c", "java -Djakarta.persistence.jdbc.url=${JDBC_DATABASE_URL} -Declipselink.ddl-generation=create-or-extend-tables -Declipselink.ddl-generation.output-mode=database -jar /usr/local/jetty/start.jar"]