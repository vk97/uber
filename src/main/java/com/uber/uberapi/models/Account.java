package com.uber.uberapi.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "account")
public class Account extends Auditable{
    private String username;
    private String password;

    @ManyToMany
    private List<Role> roles = new ArrayList<>();
}
