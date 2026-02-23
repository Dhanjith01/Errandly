package com.errandly.Errandly.user.dto;

import java.util.Set;

import com.errandly.Errandly.user.entity.Roles;

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
public class UserResponseDTO {
    private Long id;
    private String name;
    private String studentId;
    private String email;
    private String phone;
    private double reputationscore;
    private Set<Roles> roles;
    private RunnerResponseDTO runner;
}
