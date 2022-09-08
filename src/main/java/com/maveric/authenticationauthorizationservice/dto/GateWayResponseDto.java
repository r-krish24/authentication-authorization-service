package com.maveric.authenticationauthorizationservice.dto;

import io.jsonwebtoken.Claims;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GateWayResponseDto {
    boolean response;
    Claims claims;
}
