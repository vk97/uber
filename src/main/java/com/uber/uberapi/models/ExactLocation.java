package com.uber.uberapi.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "exactlocation")
@Builder
@Getter
@Setter
@NoArgsConstructor
public class ExactLocation extends Auditable{
    private Double latitude;
    private Double longitude;

    public double distanceInKm(ExactLocation otherLocation) {
        final Double R = 6371e3; // metres
        if ((latitude.equals(otherLocation.getLatitude())) && (longitude.equals(otherLocation.getLongitude()))) {
            return 0;
        }
        double theta = longitude - otherLocation.longitude;
        double dist = Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(otherLocation.latitude)) + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(other.latitude)) * Math.cos(Math.toRadians(theta));
        return Math.toDegrees(Math.acos(dist)) * 60 * 1.85316;
    }
}
