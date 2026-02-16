package com.flp.ddd.dddlibrary.lending.application;

import com.flp.ddd.dddlibrary.lending.domain.CopyId;
import com.flp.ddd.dddlibrary.lending.domain.LoanCopyRequest;
import com.flp.ddd.dddlibrary.lending.domain.UserId;
import com.flp.ddd.dddlibrary.lending.domain.exceptions.CopyIsRentedException;
import com.flp.ddd.dddlibrary.lending.infrastructure.persistence.TestLoanRepository;
import com.flp.ddd.dddlibrary.lending.infrastructure.persistence.loan.LoanEntity;
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
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(MockitoExtension.class)
@Transactional
public class RentBookUseCaseTest {
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
    private RentBookUseCase rentBookUseCase;
    @Autowired
    private TestLoanRepository loanRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE loans");
        jdbcTemplate.execute("TRUNCATE TABLE users");
    }

    @Test
    void shouldRentAnExistingAvailableBook() {
        UserId userId = new UserId();
        CopyId copyId = new CopyId();
        LoanCopyRequest request = new LoanCopyRequest(userId, copyId);
        rentBookUseCase.execute(request);

        LoanEntity loanEntity = loanRepository.findByUserIdAndCopyId(userId, copyId)
                .orElseThrow(() -> new AssertionError("Loan not found"));

        assertThat(loanEntity.getUserId().id()).isEqualTo(userId.id());
        assertThat(loanEntity.getCopyId().id()).isEqualTo(copyId.id());
        assertThat(loanEntity.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldThrowCopyIsRentedExceptionWhenTryingToLendSameCopy() {
        UserId user1Id = new UserId();
        UserId user2Id = new UserId();
        CopyId copyId = new CopyId();
        LoanCopyRequest request1 = new LoanCopyRequest(user1Id, copyId);
        LoanCopyRequest request2= new LoanCopyRequest(user2Id, copyId);
        rentBookUseCase.execute(request1);

        assertThrows(CopyIsRentedException.class, () -> rentBookUseCase.execute(request2));
    }
}
