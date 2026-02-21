package com.flp.ddd.dddlibrary.lending.application;

import com.flp.ddd.dddlibrary.UseCase;
import com.flp.ddd.dddlibrary.lending.domain.copy.CopyDTO;
import com.flp.ddd.dddlibrary.lending.domain.copy.CopyId;
import com.flp.ddd.dddlibrary.lending.domain.copy.CopyRepository;
import com.flp.ddd.dddlibrary.lending.domain.exceptions.CopyIsRentedException;
import com.flp.ddd.dddlibrary.lending.domain.loan.Loan;
import com.flp.ddd.dddlibrary.lending.domain.loan.LoanId;
import com.flp.ddd.dddlibrary.lending.domain.loan.LoanRepository;
import com.flp.ddd.dddlibrary.lending.domain.requests.LoanCopyRequest;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class RentBookUseCase {
    private final LoanRepository loanRepository;
    private final CopyRepository copyRepository;
    private final Cache<UUID, CopyDTO> cache;

    public void execute(LoanCopyRequest request) {
        Loan loan = Loan.builder()
                .id(new LoanId())
                .copyId(request.copyId())
                .userId(request.userId())
                .createdAt(LocalDateTime.now())
                .expectedReturnedDate(LocalDate.now().plusWeeks(1))
                .build();
        CopyDTO copyDTO = copyRepository.findByCopyId(request.copyId());

        if (copyDTO == null || !copyDTO.available()) {
            throw new CopyIsRentedException("The copy is already rented");
        }

        loanRepository.save(loan);
        copyRepository.save(new CopyDTO(new CopyId(request.copyId().id()), false));
    }
}
