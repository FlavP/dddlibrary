package com.flp.ddd.dddlibrary.lending.infrastructure.persistence.loan;

import com.flp.ddd.dddlibrary.lending.domain.copy.CopyId;
import com.flp.ddd.dddlibrary.lending.domain.loan.LoanId;
import com.flp.ddd.dddlibrary.lending.domain.user.UserId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "loans")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "loan_id"))
    private LoanId loanId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "user_id"))
    private UserId userId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "copy_id"))
    private CopyId copyId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "expected_returned_date")
    private LocalDate expectedReturnedDate;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;
}
