package com.maveric.authenticationauthorizationservice.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDto {

    @NotNull(message = "Email Id is mandatory")
    private String email;
    @NotNull(message = "Password is mandatory")
    private String password;
}
