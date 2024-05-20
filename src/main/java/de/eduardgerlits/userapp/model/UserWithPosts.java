package de.eduardgerlits.userapp.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserWithPosts {

    @JsonUnwrapped
    private User user;

    private List<UserPost> userPosts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserWithPosts)) return false;
        return user.equals(((UserWithPosts) o).user)
                && userPosts.equals(((UserWithPosts) o).userPosts);
    }
}
