package com.bufete.backend.Dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthResponseDTO {
    private String accessToken;
    private final String tokenType = "Bearer";
    private String email;
    private String rol;
    private String name;
}