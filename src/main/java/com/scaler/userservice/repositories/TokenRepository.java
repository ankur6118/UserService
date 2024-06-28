package com.scaler.userservice.repositories;

import com.scaler.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Override
    Token save(Token token);

    //Select * from Token where value=<> and is_deleted=false;
    Optional<Token> findByValueAndDeleted(String value, boolean isDeleted);
}
