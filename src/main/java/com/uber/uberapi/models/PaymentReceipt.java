package com.uber.uberapi.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "paymentreceipt")
public class PaymentReceipt extends Auditable{
    private Double amount;

    private String details;
    @ManyToOne
    private PaymentGateway paymentGateway;
}
