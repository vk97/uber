package com.uber.uberapi.models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name ="namedLocation")
public class NamedLocation extends Auditable{
    @OneToOne
    private ExactLocation exactLocation;
}
