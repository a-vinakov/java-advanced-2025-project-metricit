package ru.otus.vinakov.calendar.grpc;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@ConditionalOnProperty(name = "grpc.active", havingValue = "true")
public class GrpcServer {

    private final Server server;

    public GrpcServer(@Value("${grpc.port}") Integer port, List<BindableService> services) {
        ServerBuilder<?> builder = ServerBuilder.forPort(port);
        services.forEach(builder::addService);
        server = builder.build();
    }

    @PostConstruct
    public void start() throws IOException {
        server.start();
    }

    @PreDestroy
    public void stop() {
        server.shutdown();
    }

}
