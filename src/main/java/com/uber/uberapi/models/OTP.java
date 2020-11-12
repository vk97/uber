package com.uber.uberapi.models;

import com.uber.uberapi.exceptions.InvalidOTPException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Builder
@Table(name = "otp")
public class OTP extends Auditable{
    private String code;
    private String sentToNumber;

    public static OTP make(String phoneNumber) {
        return OTP.builder()
                .code("0000") // random number generator
                .sentToNumber(phoneNumber)
                .build();
    }

    public Boolean validateEnteredOTP(OTP otp, Integer rideStartOTPExpiryMinutes) {
        if(!code.equals(otp.getCode())){
            throw new InvalidOTPException();
        }
//        check for expiry
        return true;
    }
}
