# =========================================================================
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build

COPY . .

RUN mvn clean package -pl gateway -am -DskipTests

RUN java -Djarmode=layertools -jar gateway/target/gateway-1.0-SNAPSHOT.jar extract

# =========================================================================
FROM eclipse-temurin:21-jre

RUN groupadd -r spring && useradd -r -g spring spring
USER spring

WORKDIR /app

COPY --from=builder /build/dependencies/ ./
COPY --from=builder /build/spring-boot-loader/ ./
COPY --from=builder /build/snapshot-dependencies/ ./
COPY --from=builder /build/application/ ./

ENV SPRING_KAFKA_BOOTSTRAP_SERVERS=metric-kafka:9092
ENV SPRING_KAFKA_TOPIC_METRIC=metric-event-topic

ENV SERVICE_ANALYTIC_URL=http://localhost:8083
ENV SERVICE_METRIC_URL=http://localhost:8081

ENV SERVER_PORT=8080
ENV SERVER_NETTY_CONNECTION_TIMEOUT=10m
ENV SERVER_NETTY_IDLE_TIMEOUT=10m

EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]