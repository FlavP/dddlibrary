package com.flp.ddd.dddlibrary.lending.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    @NotNull
    private UserId id;
    @NotBlank
    private String username;
    @NotBlank
    @Email
    private String email;
}
