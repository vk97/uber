package com.uber.uberapi.models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "paymentgateway")
public class PaymentGateway extends Auditable{
    private String name;
}
