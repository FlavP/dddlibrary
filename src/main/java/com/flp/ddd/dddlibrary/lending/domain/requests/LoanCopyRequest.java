package com.flp.ddd.dddlibrary.lending.domain.requests;

import com.flp.ddd.dddlibrary.lending.domain.copy.CopyId;
import com.flp.ddd.dddlibrary.lending.domain.user.UserId;

public record LoanCopyRequest(
        UserId userId,
        CopyId copyId
) {
}
