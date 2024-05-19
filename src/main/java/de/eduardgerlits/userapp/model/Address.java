package de.eduardgerlits.userapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Address {

    private String street;

    private String suite;

    private String city;

    private String zipcode;

    private Geo geo;

}
