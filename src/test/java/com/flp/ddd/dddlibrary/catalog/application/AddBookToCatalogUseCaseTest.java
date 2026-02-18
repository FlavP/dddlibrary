package com.flp.ddd.dddlibrary.catalog.application;

import com.flp.ddd.dddlibrary.catalog.domain.book.Isbn;
import com.flp.ddd.dddlibrary.shared.events.CopyCreatedEvent;
import com.flp.ddd.dddlibrary.catalog.domain.exceptions.BookNotFoundException;
import com.flp.ddd.dddlibrary.catalog.infrastructure.persistence.TestBookRepository;
import com.flp.ddd.dddlibrary.catalog.infrastructure.persistence.book.BookEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(MockitoExtension.class)
@Transactional
@RecordApplicationEvents
public class AddBookToCatalogUseCaseTest {

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

    @MockitoBean
    BookSearchService bookSearchService;
    @Autowired
    AddBookToCatalogUseCase addBookToCatalogUseCase;
    @Autowired
    TestBookRepository testBookRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ApplicationEvents applicationEvents;

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
    void shouldCreateNewBookAndNewCopyInTheDatabaseAndPublishCopyCreatedEvent() {
        Isbn isbn = new Isbn("9780132350884");
        BookInformation bookInformation = new BookInformation("Clean Code");
        when(bookSearchService.search(isbn)).thenReturn(bookInformation);

        addBookToCatalogUseCase.execute(isbn);
        // to query for a random record
//        Integer record = jdbcTemplate.queryForObject(
//                "SELECT COUNT(*) FROM books WHERE title = ? AND isbn = ?",
//                Integer.class,
//                "Clean Code", "9780132350884"
//        );

        BookEntity bookEntity = testBookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new AssertionError("Book not found"));
        long eventCount = applicationEvents.stream(CopyCreatedEvent.class).count();
        CopyCreatedEvent copyCreatedEvent = applicationEvents.stream(CopyCreatedEvent.class)
                        .findFirst()
                                .orElseThrow();

        assertThat(bookEntity.getTitle()).isEqualTo("Clean Code");
        assertThat(bookEntity.getCopies()).hasSize(1);
        assertThat(bookEntity.getCopies().getFirst().getBook().getBookId())
                .isEqualTo(bookEntity.getBookId());
        assertThat(eventCount).isEqualTo(1);
        assertThat(copyCreatedEvent.copyId().toString())
                .isEqualTo(bookEntity.getCopies().getFirst().getCopyId().toString());
    }

    @Test
    void shouldThrowBookNotFoundExceptionWhenTheBookIsNotFoundInOpenLibrary() {
        when(bookSearchService.search(any(Isbn.class))).thenThrow(new BookNotFoundException("Book not found"));
        Isbn isbn = new Isbn("9780132350884");
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> addBookToCatalogUseCase.execute(isbn));

        assertThat(exception.getMessage()).isEqualTo("Book not found");
        Integer record = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM books WHERE title = ? AND isbn = ?", Integer.class, "Clean Code", "9780132350884");

        assertThat(record).isEqualTo(0);
    }
}
