FROM maven:3.9.4-eclipse-temurin-17-alpine AS build

WORKDIR /app

COPY src ./src
COPY pom.xml .

RUN mvn clean package

ENTRYPOINT ["java", "-jar", "target/manageclients-0.0.1.jar"]

FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/target/manageclients-0.0.1.jar manageclients.jar

ENTRYPOINT ["java", "-jar", "manageclients.jar"]