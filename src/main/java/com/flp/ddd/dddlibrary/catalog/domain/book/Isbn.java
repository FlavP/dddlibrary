package com.flp.ddd.dddlibrary.catalog.domain.book;

import org.apache.commons.validator.routines.ISBNValidator;

public record Isbn(String value) {
    private static ISBNValidator VALIDATOR = new ISBNValidator();

    public Isbn {
        if (!VALIDATOR.isValid(value)) {
            throw new IllegalArgumentException("invalid isbn: " + value);
        }
    }
}
