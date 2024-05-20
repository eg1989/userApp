package de.eduardgerlits.userapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserPost {

    private int userId;

    private int id;

    private String title;

    private String body;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPost)) return false;
        return userId == ((UserPost) o).userId
                && id == ((UserPost) o).id
                && title.equals(((UserPost) o).title)
                && body.equals(((UserPost) o).body);
    }

}
