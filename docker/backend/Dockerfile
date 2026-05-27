FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

COPY pom.xml .
COPY checkstyle-suppressions.xml* ./
COPY configuration_1_2.dtd* ./

RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

RUN mv target/*-SNAPSHOT.jar target/app.jar || mv target/*.jar target/app.jar

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]