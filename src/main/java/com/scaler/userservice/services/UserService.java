package com.scaler.userservice.services;


import com.scaler.userservice.models.Token;
import com.scaler.userservice.models.User;
import com.scaler.userservice.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncode;

    UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncode){
        this.userRepository = userRepository;
        this.bCryptPasswordEncode = bCryptPasswordEncode;
    }

    public User signUp(String name, String email, String password)
    {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            //Either throw an exception that user already present
            return optionalUser.get();
        }

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(bCryptPasswordEncode.encode(password));
        return userRepository.save(user);
    }

    public Token login(String email, String password){
        return null;
    }

    public void logout(Token token){

    }
}
