package com.flp.ddd.dddlibrary.lending.infrastructure.persistence.loan;

import com.flp.ddd.dddlibrary.lending.domain.Loan;
import com.flp.ddd.dddlibrary.lending.domain.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DbLoanRepository implements LoanRepository {
    private final JpaLoanRepository jpaLoanRepository;

    @Override
    public void save(Loan loan) {
        LoanEntity loanEntity = toEntity(loan);
        jpaLoanRepository.save(loanEntity);
    }

    private LoanEntity toEntity(Loan loan) {
        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setLoanId(loan.getId());
        loanEntity.setUserId(loan.getUserId());
        loanEntity.setCopyId(loan.getCopyId());
        loanEntity.setCreatedAt(loan.getCreatedAt());
        loanEntity.setExpectedReturnedDate(loan.getExpectedReturnedDate());
        return loanEntity;
    }
}
