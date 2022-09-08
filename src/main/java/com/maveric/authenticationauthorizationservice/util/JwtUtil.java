package com.maveric.authenticationauthorizationservice.util;

import com.maveric.authenticationauthorizationservice.dto.GateWayResponseDto;
import com.maveric.authenticationauthorizationservice.model.UserPrincipal;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserPrincipal userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public GateWayResponseDto validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            GateWayResponseDto gateWayResponseDto = new GateWayResponseDto(true,extractAllClaims(token));
            return gateWayResponseDto;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature trace: {}"+ e);
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token trace: {}"+ e);
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token trace: {}"+ e);
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token trace: {}"+ e);
        } catch (IllegalArgumentException e) {
            System.out.println("JWT token compact of handler are invalid trace: {}"+e);
        }
        return new GateWayResponseDto(false,null);
    }
}
