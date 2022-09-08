package com.maveric.authenticationauthorizationservice.service;

import com.maveric.authenticationauthorizationservice.constants.Type;
import com.maveric.authenticationauthorizationservice.dto.AuthResponseDto;
import com.maveric.authenticationauthorizationservice.dto.UserDetailsDto;
import com.maveric.authenticationauthorizationservice.exceptions.UserNotFoundException;
import com.maveric.authenticationauthorizationservice.feignconsumer.UserServiceConsumer;
import com.maveric.authenticationauthorizationservice.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.maveric.authenticationauthorizationservice.constants.Constants.USER_NOT_FOUND_MESSAGE;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserServiceConsumer userServiceConsumer;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        /*UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                ._id("12345")
                .firstName("Ram")
                .lastName("Krish")
                .address("Address")
                .gender(Type.MALE)
                .middleName("E")
                .email("ram@gmail.com")
                .phoneNumber("976482630")
                .dateOfBirth("1998-09-24")
                .password("$2a$10$03mlnKjDtEW.LkR8yiyVa.Ro0jJlgIglB8BoJ4K7A8O9z3naw/QCq").build();
        return new UserPrincipal(userDetailsDto); */
        System.out.println("Inside UserService");
        ResponseEntity<UserDetailsDto> userDetailsDto = userServiceConsumer.getUserDetailsByEmail(email);
        System.out.println(userDetailsDto.getBody());
        System.out.println(userDetailsDto.getBody().getEmail());
        System.out.println(userDetailsDto.getBody().getPassword());
        try {
            if(userDetailsDto.getBody()!=null)
            {
                System.out.println("Success");
                return new UserPrincipal(userDetailsDto.getBody());
            }
            else {
                System.out.println("Failed to Else");
                throw new UserNotFoundException(USER_NOT_FOUND_MESSAGE+email);
            }
        }
        catch(UserNotFoundException ex)
        {
            System.out.println("Failed to catch");
            throw new UserNotFoundException(USER_NOT_FOUND_MESSAGE+email);
        }
    }
}
