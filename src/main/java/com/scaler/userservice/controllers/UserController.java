package com.scaler.userservice.controllers;

import com.scaler.userservice.dtos.LogInRequestDto;
import com.scaler.userservice.dtos.LogOutRequestDto;
import com.scaler.userservice.dtos.SignUpRequestDto;
import com.scaler.userservice.dtos.UserDto;
import com.scaler.userservice.models.Token;
import com.scaler.userservice.models.User;
import com.scaler.userservice.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Token logIn(@RequestBody LogInRequestDto requestDto){
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logOut(@RequestBody LogOutRequestDto requestDto){
        return null;
    }

}
