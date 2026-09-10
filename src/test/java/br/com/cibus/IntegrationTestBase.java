package br.com.cibus;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Base para testes de integração: sobe um container MySQL descartável via Testcontainers
 * (isolado do banco de desenvolvimento) e roda as migrations do Flyway de verdade contra ele.
 * Usa o contexto web "mock" (padrão do @SpringBootTest) + MockMvc, então bate nos controllers
 * reais e no banco real, sem precisar de um servidor HTTP de verdade escutando numa porta.
 */
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public abstract class IntegrationTestBase {

    static final MySQLContainer<?> MYSQL_CONTAINER;

    static {
        MYSQL_CONTAINER = new MySQLContainer<>("mysql:8")
                .withDatabaseName("cibus_test")
                .withUsername("root")
                .withPassword("root");
        MYSQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
    }
}
