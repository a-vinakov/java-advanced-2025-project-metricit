# =========================================================================
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build

COPY . .

RUN mvn clean package -pl metric-app -am -DskipTests

RUN java -Djarmode=layertools -jar metric-app/target/metric-app-1.0-SNAPSHOT.jar extract

# =========================================================================
FROM eclipse-temurin:21-jre

RUN groupadd -r spring && useradd -r -g spring spring
USER spring

WORKDIR /app

COPY --from=builder /build/dependencies/ ./
COPY --from=builder /build/spring-boot-loader/ ./
COPY --from=builder /build/snapshot-dependencies/ ./
COPY --from=builder /build/application/ ./

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/metricdb
ENV SPRING_DATASOURCE_USERNAME=metric
ENV SPRING_DATASOURCE_PASSWORD=metric
ENV SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092

ENV GRPC_CALENDAR_HOST=localhost
ENV GRPC_CALENDAR_PORT=8981
ENV SERVER_PORT=8081

EXPOSE 8081

ENTRYPOINT ["java", "-Duser.home=/tmp", "org.springframework.boot.loader.launch.JarLauncher"]