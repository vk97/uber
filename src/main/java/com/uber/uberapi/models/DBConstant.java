package com.uber.uberapi.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "dbconstant")
public class DBConstant extends Auditable{
    @Column(unique = true,nullable = false)
    private String name;
    private String value;
}
