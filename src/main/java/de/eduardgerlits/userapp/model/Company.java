package de.eduardgerlits.userapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Company {

    private String name;

    private String catchPhrase;

    private String bs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company)) return false;
        return this.name.equals(((Company) o).name)
                && this.catchPhrase.equals(((Company) o).catchPhrase)
                && this.bs.equals(((Company) o).bs);
    }

}
