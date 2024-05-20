package de.eduardgerlits.userapp.exception;

public class UserPostsNotFoundException extends RuntimeException {

    public UserPostsNotFoundException(int id) {
        super("No posts found for User with id " + id);
    }

}
