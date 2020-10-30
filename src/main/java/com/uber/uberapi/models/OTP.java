package com.uber.uberapi.models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "otp")
public class OTP extends Auditable{
    private String code;
    private String sentToNumber;
}
