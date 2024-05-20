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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Address)) return false;
        return street.equals(((Address) o).street)
                && suite.equals(((Address) o).suite)
                && city.equals(((Address) o).city)
                && zipcode.equals(((Address) o).zipcode)
                && geo.equals(((Address) o).geo);
    }

}
