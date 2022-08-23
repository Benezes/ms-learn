FROM maven:3.8.6-openjdk-18 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17
WORKDIR /app
COPY --from=build ./app/target/*.jar ./app.jar
ARG RABBITMQ_SERVER=cursomw-rabbitmq
ARG EUREKA_SERVER=cursoms-eureka
ENTRYPOINT java -jar app.jar