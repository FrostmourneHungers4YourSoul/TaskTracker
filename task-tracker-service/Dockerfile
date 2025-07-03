FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
COPY src /usr/app/src
COPY pom.xml /usr/app
RUN mvn -f /usr/app/pom.xml clean package -DskipTests

FROM amazoncorretto:21-alpine AS build
WORKDIR /application
ARG JAR_FILE=target/*.jar
COPY --from=builder /usr/app/${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build application/dependencies/ ./
COPY --from=build application/spring-boot-loader/ ./
COPY --from=build application/snapshot-dependencies/ ./
COPY --from=build application/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
