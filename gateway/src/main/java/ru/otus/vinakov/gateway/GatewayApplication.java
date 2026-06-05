package ru.otus.vinakov.gateway;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(title = "MetricIt gateway rest api",
                description = "MetricIt gateway rest api",
                contact = @Contact(name = "Alexandr Vinakov"),
                version = "0.0.1"),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local server (for test)")
        }
)
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
