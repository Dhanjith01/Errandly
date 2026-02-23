package com.errandly.Errandly.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    private String name;
    @NotBlank
    private String studentId;
    @NotBlank
    @Email
    private String email;
    private String phone; 

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
