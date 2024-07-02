package com.scaler.userservice.security.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.scaler.userservice.models.Role;
import org.springframework.security.core.GrantedAuthority;

@JsonDeserialize
public class CustomGrantedAuthority implements GrantedAuthority {
    private String authority;

    public CustomGrantedAuthority(){}

    public CustomGrantedAuthority(Role role){
        authority = role.getValue();
    }
    @Override
    public String getAuthority() {
        return authority;
    }
}
