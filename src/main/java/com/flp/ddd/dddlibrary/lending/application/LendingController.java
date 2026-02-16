package com.flp.ddd.dddlibrary.lending.application;

import com.flp.ddd.dddlibrary.lending.domain.LoanCopyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LendingController {
    private final RentBookUseCase rentBookUseCase;

    @PostMapping("/api/lending/copy")
    public ResponseEntity<Void> save(@RequestBody LoanCopyRequest request) {
        try {
            rentBookUseCase.execute(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
