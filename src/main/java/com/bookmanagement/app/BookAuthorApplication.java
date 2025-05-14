package com.bookmanagement.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Book and Author Management API",
        version = "1.0",
        description = "API for managing books and authors"
    )
)
public class BookAuthorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookAuthorApplication.class, args);
    }
}
