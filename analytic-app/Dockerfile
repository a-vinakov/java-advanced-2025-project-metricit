# =========================================================================
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build

COPY . .

RUN mvn clean package -pl analytic-app -am -DskipTests

RUN java -Djarmode=layertools -jar analytic-app/target/analytic-app-1.0-SNAPSHOT.jar extract

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

ENV ANALYTIC_JOBS_MAX_CONCURRENT=20
ENV GATEWAY_HOST=http://localhost:8080

ENV SERVER_PORT=8083

# Открываем порт приложения
EXPOSE 8083

# Запуск приложения
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]