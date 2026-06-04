# =========================================================================
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build

COPY . .

RUN mvn clean package -pl calendar-app -am -DskipTests

RUN java -Djarmode=layertools -jar calendar-app/target/calendar-app-1.0-SNAPSHOT.jar extract

# =========================================================================
FROM eclipse-temurin:21-jre

RUN groupadd -r spring && useradd -r -g spring spring
USER spring

WORKDIR /app

COPY --from=builder /build/dependencies/ ./
COPY --from=builder /build/spring-boot-loader/ ./
COPY --from=builder /build/snapshot-dependencies/ ./
COPY --from=builder /build/application/ ./

ENV SPRING_MONGODB_URI=mongodb://localhost:27017/production_calendar_db?authSource=admin
ENV GRPC_PORT=8981
ENV SERVER_PORT=8082

EXPOSE 8082 8981

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]