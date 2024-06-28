package com.scaler.userservice.services;


import com.scaler.userservice.exceptions.InvalidPasswordException;
import com.scaler.userservice.exceptions.InvalidTokenException;
import com.scaler.userservice.models.Token;
import com.scaler.userservice.models.User;
import com.scaler.userservice.repositories.TokenRepository;
import com.scaler.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.random.RandomGenerator;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncode;
    private TokenRepository tokenRepository;

    UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncode,
                TokenRepository tokenRepository){
        this.userRepository = userRepository;
        this.bCryptPasswordEncode = bCryptPasswordEncode;
        this.tokenRepository = tokenRepository;
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

    public Token login(String email, String password) throws InvalidPasswordException {
        /*
         1. Check if the user exist or not with given email & password
         2. If not, throw an exception or redirect the user to signup.
         3. If exists then compare the incoming password against the store in DB one.
         4. If password matches then login successfully and return the new token.
         */
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()){
            //User with given email isn't present in the DB
            return null;
        }

        User user = optionalUser.get();

        if(!bCryptPasswordEncode.matches(password,user.getHashedPassword())){
            //If password doesn't matches throw invalid Exception
            throw new InvalidPasswordException("Please enter a valid Password");
        }

        Token token = generateToken(user);

        return tokenRepository.save(token);


    }

    private Token generateToken(User user){
        LocalDate currentTime = LocalDate.now(); //currentTime
        LocalDate thirtyDaysFromNow = currentTime.plusDays(30);
        //Setting expirtAt after 30days
        Date expiryDate = Date.from(thirtyDaysFromNow.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();
        token.setExpiryAt(expiryDate);
        token.setValue(RandomStringUtils.randomAlphanumeric(120));
        token.setUser(user);
        return token;
    }

    public void logout(String tokenValue) throws InvalidTokenException {
        //First validate if token is present in DB and is_deleted flag is flase
        Optional<Token> optionalToken= tokenRepository.findByValueAndDeleted(
                tokenValue,
                false
        );

        if(optionalToken.isEmpty())
            throw new InvalidTokenException("Invalid Token Passed.");

        Token token = optionalToken.get();
        token.setDeleted(true);
        tokenRepository.save(token);
    }

    public User validateToken(String tokenValue) throws InvalidTokenException {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeleted(tokenValue,false);

        if(optionalToken.isEmpty())
            throw new InvalidTokenException("Invalid Token Passed.");

        return optionalToken.get().getUser();
    }
}
