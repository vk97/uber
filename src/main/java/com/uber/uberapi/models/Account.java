package com.uber.uberapi.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "account")
public class Account extends Auditable{
    private String username;
    private String password;

//    @ManyToMany(fetch = FetchType.EAGER)
    @ManyToMany
    private List<Role> roles = new ArrayList<>();
}
