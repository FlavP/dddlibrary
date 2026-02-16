package com.flp.ddd.dddlibrary.lending.domain;

import org.springframework.util.Assert;

import java.util.UUID;

public record LoanId(UUID id) {
    public LoanId {
        Assert.notNull(id, "The id cannot be null");
    }

    public LoanId() {
        this(UUID.randomUUID());
    }
}
