
FROM openjdk:8-jdk-alpine as builder

WORKDIR /app/msvc-gateway

COPY ./pom.xml /app
COPY ./msvc-gateway/.mvn ./.mvn
COPY ./msvc-gateway/mvnw .
COPY ./msvc-gateway/pom.xml .


RUN ./mvnw dependency:go-offline
COPY ./msvc-gateway/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:8-jdk-alpine
WORKDIR /app

RUN mkdir ./logs
COPY --from=builder /app/msvc-gateway/target/msvc-gateway-0.0.1-SNAPSHOT.jar .

EXPOSE 8090
CMD ["java","-jar","msvc-gateway-0.0.1-SNAPSHOT.jar"]