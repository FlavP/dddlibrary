package com.flp.ddd.dddlibrary.lending.application;

import com.flp.ddd.dddlibrary.UseCase;
import com.flp.ddd.dddlibrary.lending.domain.copy.CopyDTO;
import com.flp.ddd.dddlibrary.lending.domain.copy.CopyId;
import com.flp.ddd.dddlibrary.lending.domain.copy.CopyRepository;
import com.flp.ddd.dddlibrary.lending.domain.exceptions.CopyIsRentedException;
import com.flp.ddd.dddlibrary.lending.domain.exceptions.CopyNotFoundException;
import com.flp.ddd.dddlibrary.lending.domain.loan.Loan;
import com.flp.ddd.dddlibrary.lending.domain.loan.LoanId;
import com.flp.ddd.dddlibrary.lending.domain.loan.LoanRepository;
import com.flp.ddd.dddlibrary.lending.domain.requests.LoanCopyRequest;
import com.flp.ddd.dddlibrary.shared.events.CopyUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.time.LocalDateTime;

@UseCase
@RequiredArgsConstructor
public class RentBookUseCase {
    private final LoanRepository loanRepository;
    private final CopyRepository copyRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void execute(LoanCopyRequest request) {
        Loan loan = Loan.builder()
                .id(new LoanId())
                .copyId(request.copyId())
                .userId(request.userId())
                .createdAt(LocalDateTime.now())
                .expectedReturnedDate(LocalDate.now().plusWeeks(1))
                .build();
        CopyDTO copyDTO = copyRepository.findByCopyId(request.copyId());

        if (copyDTO == null) {
            throw new CopyNotFoundException("Copy does not exist");
        }

        if (!copyDTO.available()) {
            throw new CopyIsRentedException("The copy is already rented");
        }

        loanRepository.save(loan);
        copyRepository.save(new CopyDTO(new CopyId(request.copyId().id()), false));
        eventPublisher.publishEvent(new CopyUpdatedEvent(
                request.copyId().id(),
                true
        ));
    }
}
