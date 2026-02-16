package com.flp.ddd.dddlibrary.lending.application;

import com.flp.ddd.dddlibrary.UseCase;
import com.flp.ddd.dddlibrary.lending.domain.Loan;
import com.flp.ddd.dddlibrary.lending.domain.LoanCopyRequest;
import com.flp.ddd.dddlibrary.lending.domain.LoanId;
import com.flp.ddd.dddlibrary.lending.domain.LoanRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@UseCase
@RequiredArgsConstructor
public class RentBookUseCase {
    private final LoanRepository loanRepository;

    public void execute(LoanCopyRequest request) {
        Loan loan = Loan.builder()
                .id(new LoanId())
                .copyId(request.copyId())
                .userId(request.userId())
                .createdAt(LocalDateTime.now())
                .expectedReturnedDate(LocalDate.now().plusWeeks(1))
                .build();

        loanRepository.save(loan);
    }
}
