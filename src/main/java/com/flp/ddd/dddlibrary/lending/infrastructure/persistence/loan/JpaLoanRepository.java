package com.flp.ddd.dddlibrary.lending.infrastructure.persistence.loan;

import org.springframework.data.repository.CrudRepository;

public interface JpaLoanRepository extends CrudRepository<LoanEntity, Integer> {
}
