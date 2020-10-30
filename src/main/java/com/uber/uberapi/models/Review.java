package com.uber.uberapi.models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "review")
public class Review extends Auditable{
    private Integer ratingOutOfFive;
    private String note;
}
