package com.scaler.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Token extends BaseModel{

    private String value;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Date expiryAt;
}
