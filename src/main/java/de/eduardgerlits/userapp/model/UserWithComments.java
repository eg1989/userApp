package de.eduardgerlits.userapp.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserWithComments {

    @JsonUnwrapped
    private User user;

    private List<UserComment> userComments;
}
