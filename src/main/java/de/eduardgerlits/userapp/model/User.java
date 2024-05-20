package de.eduardgerlits.userapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class User {

    private int id;

    private String name;

    private String username;

    private String email;

    private Address address;

    private String phone;

    private String website;

    private Company company;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return this.id == ((User) o).id
                && this.name.equals(((User) o).name)
                && this.username.equals(((User) o).username)
                && this.email.equals(((User) o).email)
                && this.address.equals(((User) o).address)
                && this.phone.equals(((User) o).phone)
                && this.website.equals(((User) o).website)
                && this.company.equals(((User) o).company);
    }

}
