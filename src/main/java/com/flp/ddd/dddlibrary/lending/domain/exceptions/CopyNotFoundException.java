package com.flp.ddd.dddlibrary.lending.domain.exceptions;

public class CopyNotFoundException extends RuntimeException {
    public CopyNotFoundException(String message) {
        super(message);
    }
}

