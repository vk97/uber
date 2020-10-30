package com.uber.uberapi.models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "exactlocation")
public class ExactLocation extends Auditable{
    private String latitude;
    private String longitude;
}
