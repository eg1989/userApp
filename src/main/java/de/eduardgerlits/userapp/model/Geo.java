package de.eduardgerlits.userapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Geo {

    private String lat;

    private String lng;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Geo)) return false;
        return lat.equals(((Geo) o).lat) && lng.equals(((Geo) o).lng);
    }

}
