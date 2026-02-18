package com.flp.ddd.dddlibrary.lending.application;

import com.flp.ddd.dddlibrary.UseCase;
import com.flp.ddd.dddlibrary.lending.domain.*;
import com.flp.ddd.dddlibrary.lending.domain.exceptions.CopyIsRentedException;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class RentBookUseCase {
    private final LoanRepository loanRepository;
    private final Cache<UUID, CopyDTO> cache;

    public void execute(LoanCopyRequest request) {
        Loan loan = Loan.builder()
                .id(new LoanId())
                .copyId(request.copyId())
                .userId(request.userId())
                .createdAt(LocalDateTime.now())
                .expectedReturnedDate(LocalDate.now().plusWeeks(1))
                .build();
        CopyDTO copyDTO = cache.getIfPresent(request.copyId().id());

        if (copyDTO == null || !copyDTO.available()) {
            throw new CopyIsRentedException("The copy is already rented");
        }

        loanRepository.save(loan);
        cache.put(request.copyId().id(), new CopyDTO(new CopyId(request.copyId().id()), false));
    }
}
