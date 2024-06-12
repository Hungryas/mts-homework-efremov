package org.example.dao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@Builder(toBuilder = true)
public class User {

    @NotNull
    @Size(min = 3, max = 250, message = "size must be between 3 and 250")
    private String name;

    @Email
    private String email;
}