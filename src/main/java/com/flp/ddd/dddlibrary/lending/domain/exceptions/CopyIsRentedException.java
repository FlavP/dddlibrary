package com.flp.ddd.dddlibrary.lending.domain.exceptions;

public class CopyIsRentedException extends RuntimeException {
    public CopyIsRentedException(String message) {
        super(message);
    }
}
