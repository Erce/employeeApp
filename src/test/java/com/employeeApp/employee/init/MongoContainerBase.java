package com.employeeApp.employee.init;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class MongoContainerBase {
    // Creating MongoDB container for testing
    @Container
    private static final MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:latest"));
    @DynamicPropertySource
    static void mongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
        registry.add("spring.data.mongodb.database", () -> "testDB");
    }

    @BeforeAll
    public static void beforeAll() {
        mongoDBContainer.start();
    }
}
