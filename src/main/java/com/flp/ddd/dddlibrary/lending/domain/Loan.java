package com.flp.ddd.dddlibrary.lending.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class Loan {
    private LoanId id;
    private CopyId copyId;
    private UserId userId;
    private LocalDateTime createdAt;
    private LocalDate expectedReturnedDate;
    private LocalDateTime returnedAt;
}
