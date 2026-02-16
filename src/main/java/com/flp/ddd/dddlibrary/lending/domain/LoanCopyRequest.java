package com.flp.ddd.dddlibrary.lending.domain;

public record LoanCopyRequest(
        UserId userId,
        CopyId copyId
) {
}
