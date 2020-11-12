package com.uber.uberapi.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "review")
@Getter
@Setter
@Builder
public class Review extends Auditable{
    private Integer ratingOutOfFive;
    private String note;
}
