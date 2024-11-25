FROM openjdk:22

WORKDIR /app

COPY target/backend.jar app.jar

EXPOSE 8080

ENV DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/case-study

ENV DATABASE_USERNAME=postgres

ENV DATABASE_PASSWORD=0000

ENTRYPOINT ["java", "-jar", "app.jar"]