package com.flp.ddd.dddlibrary.lending.infrastructure.persistence;

import com.flp.ddd.dddlibrary.lending.domain.copy.CopyId;
import com.flp.ddd.dddlibrary.lending.domain.user.UserId;
import com.flp.ddd.dddlibrary.lending.infrastructure.persistence.loan.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestLoanRepository extends JpaRepository<LoanEntity, Integer> {
    Optional<LoanEntity> findByUserIdAndCopyId(UserId userId, CopyId copyId);
}
