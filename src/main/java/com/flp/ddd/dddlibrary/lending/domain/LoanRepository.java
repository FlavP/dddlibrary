package com.flp.ddd.dddlibrary.lending.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository {
    void save(Loan loan);
}
