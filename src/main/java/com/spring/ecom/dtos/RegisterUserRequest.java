package com.spring.ecom.dtos;

import com.spring.ecom.validations.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message ="Name must be less than 255 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "email must be valid")
    @Lowercase(message = "email must be in lowercase")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6 , message = "Min length is 6 characters")
    private String password;
}
