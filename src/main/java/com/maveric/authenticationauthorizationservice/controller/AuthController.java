package com.maveric.authenticationauthorizationservice.controller;

import com.maveric.authenticationauthorizationservice.dto.*;
import com.maveric.authenticationauthorizationservice.exceptions.AccountCreationFailedException;
import com.maveric.authenticationauthorizationservice.exceptions.InvalidCredentialsException;
import com.maveric.authenticationauthorizationservice.feignconsumer.UserServiceConsumer;
import com.maveric.authenticationauthorizationservice.model.UserPrincipal;
import com.maveric.authenticationauthorizationservice.service.UserService;
import com.maveric.authenticationauthorizationservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    UserServiceConsumer userServiceConsumer;

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("auth/login")
    public ResponseEntity<AuthResponseDto> authLogin(@Valid @RequestBody AuthRequestDto authRequestDto) throws Exception {
        System.out.println(authRequestDto.getEmail() +"---"+authRequestDto.getPassword());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            System.out.println("Exception of bad creds");
            throw new InvalidCredentialsException("Incorrect username or password");
        }
        System.out.println("------check1");
        final UserPrincipal userPrincipal = (UserPrincipal)userService
                .loadUserByUsername(authRequestDto.getEmail());

        final String jwt = jwtTokenUtil.generateToken(userPrincipal);
        AuthResponseDto authResponseDto = new AuthResponseDto();
        authResponseDto.setToken(jwt);
        authResponseDto.setUser(userPrincipal.getUser());
        return new ResponseEntity<AuthResponseDto>(authResponseDto, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("auth/signup")
    public ResponseEntity<AuthResponseDto> authSignUp(@Valid @RequestBody UserDetailsDto userDetailsDto) {
        ResponseEntity<UserDetailsDto> userDetailsDtoRespEntity = userServiceConsumer.createUser(userDetailsDto);
        UserDetailsDto userDetailsDtoResp = userDetailsDtoRespEntity.getBody();
        AuthResponseDto authResponseDto = new AuthResponseDto();
        if(userDetailsDtoResp!=null)
        {
            UserPrincipal userPrincipal = new UserPrincipal(userDetailsDtoResp);
            authResponseDto.setToken(jwtTokenUtil.generateToken(userPrincipal));
            authResponseDto.setUser(userPrincipal.getUser());
        }
        else {
                throw new AccountCreationFailedException("Account creation failed");
        }
        return new ResponseEntity<AuthResponseDto>(authResponseDto, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("auth/validate")
    public ResponseEntity<GateWayResponseDto> validateToken(@Valid @RequestBody GateWayRequestDto gateWayRequestDto) {
        System.out.println("Inside validateToken");
        GateWayResponseDto resp = jwtTokenUtil.validateToken(gateWayRequestDto.getToken());
        return new ResponseEntity<GateWayResponseDto>(resp, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/hello")
    public String sampleAPI() {
        return "Hello Maveric!";
    }


}
