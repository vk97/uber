package com.uber.uberapi.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "car")
@Getter
@Setter
public class Car extends Auditable{
    @OneToOne
    private Driver driver;

    @ManyToOne
    private Color color;

    private String plateNumber;

    private String brandAndModel;

    @Enumerated(value = EnumType.STRING)
    private CarType carType;
}
