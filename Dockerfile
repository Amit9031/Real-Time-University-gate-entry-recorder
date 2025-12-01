FROM eclipse-temurin:17-jdk-jammy as build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN ./mvnw -q -DskipTests package || mvn -q -DskipTests package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /app/target/gate-entry-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]


