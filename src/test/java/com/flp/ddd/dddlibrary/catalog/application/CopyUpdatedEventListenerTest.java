package com.flp.ddd.dddlibrary.catalog.application;

import com.flp.ddd.dddlibrary.catalog.infrastructure.persistence.TestBookRepository;
import com.flp.ddd.dddlibrary.catalog.infrastructure.persistence.TestCopyRepository;
import com.flp.ddd.dddlibrary.catalog.infrastructure.persistence.book.BookEntity;
import com.flp.ddd.dddlibrary.catalog.infrastructure.persistence.copy.CopyEntity;
import com.flp.ddd.dddlibrary.catalog.domain.book.Isbn;
import com.flp.ddd.dddlibrary.shared.events.CopyUpdatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(MockitoExtension.class)
@RecordApplicationEvents
@EnableAsync
public class CopyUpdatedEventListenerTest {

    static org.testcontainers.containers.PostgreSQLContainer<?> postgres;

    static {
        System.setProperty("testcontainers.ryuk.disabled", "true");
    }

    static {
        String home = System.getProperty("user.home");
        System.setProperty("DOCKER_HOST", "unix://" + home + "/.rd/docker.sock");

        postgres = new org.testcontainers.containers.PostgreSQLContainer<>(DockerImageName.parse("postgres:17.4")).withReuse(true);
        postgres.start();
    }

    @Autowired
    private ApplicationEventPublisher testEventPublisher;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TestBookRepository testBookRepository;
    @Autowired
    private TestCopyRepository testCopyRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE copies CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE books CASCADE");
    }

    @Test
    void shouldHandleDuplicateLoanEventsFollowedByReturn() {
        UUID bookId = UUID.randomUUID();
        UUID copyId = UUID.randomUUID();

        CopyEntity copyEntity = CopyEntity.builder()
                .copyId(copyId)
                .barcode("ABC123")
                .isAvailable(true)
                .build();

        BookEntity bookEntity = new BookEntity(
                null,
                bookId,
                "Clean Code",
                List.of(copyEntity),
                new Isbn("9780132350884")
        );
        copyEntity.setBook(bookEntity);
        testBookRepository.save(bookEntity);

        CopyEntity savedCopy = testCopyRepository.findByCopyId(copyId).orElseThrow();
        assertThat(savedCopy.isAvailable()).isTrue();

        testEventPublisher.publishEvent(new CopyUpdatedEvent(copyId, true));

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            CopyEntity loanedCopy = testCopyRepository.findByCopyId(copyId).orElseThrow();
            assertThat(loanedCopy.isAvailable()).isFalse();
        });

        testEventPublisher.publishEvent(new CopyUpdatedEvent(copyId, false));

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            CopyEntity returnedCopy = testCopyRepository.findByCopyId(copyId).orElseThrow();
            assertThat(returnedCopy.isAvailable()).isTrue();
        });
    }
}
