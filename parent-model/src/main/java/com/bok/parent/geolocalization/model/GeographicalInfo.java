package com.bok.parent.geolocalization.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class GeographicalInfo {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String city;

    @Column
    private String country;

    @Column
    private String postalCode;

    @Column
    private String state;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private Integer accuracyRadius;

}
