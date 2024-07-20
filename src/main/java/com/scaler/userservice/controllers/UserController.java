package com.scaler.userservice.controllers;

import com.scaler.userservice.dtos.*;
import com.scaler.userservice.exceptions.InvalidPasswordException;
import com.scaler.userservice.exceptions.InvalidTokenException;
import com.scaler.userservice.models.Token;
import com.scaler.userservice.models.User;
import com.scaler.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup") // localhost:8080/users/signup
    public UserDto signUp(@RequestBody SignUpRequestDto requestDto){
        User user = userService.signUp(
                requestDto.getName(), requestDto.getEmail(), requestDto.getPassword()
        );

        //need to convert user object into userDto
        return UserDto.from(user);
    }

    @PostMapping("/login")
    public LogInResponseDto logIn(@RequestBody LogInRequestDto requestDto) throws InvalidPasswordException {
        Token token =  userService.login(requestDto.getEmail(), requestDto.getPassword());
        LogInResponseDto responseDto = new LogInResponseDto();
        responseDto.setToken(token);
        return responseDto;
    }



    @PostMapping("/logout")
    public ResponseEntity<Void> logOut(@RequestBody LogOutRequestDto requestDto){
        ResponseEntity<Void> responseEntity = null;
        try {
            userService.logout(requestDto.getToken());
            responseEntity = new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidTokenException e) {
            System.out.println("Something went wrong.");
            responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @PostMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable String token) throws InvalidTokenException {
        return UserDto.from(userService.validateToken(token));
    }

    @GetMapping("/{id}")
    private String getUserDetails(@PathVariable("id") Long userId){
        System.out.println("Got the request");
        return "Hello from user with Id" + userId ;
    }

}
