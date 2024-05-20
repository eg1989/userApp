package de.eduardgerlits.userapp.exception;

public class ApiUnreachableException extends RuntimeException{

    public ApiUnreachableException(final String fullPath) {
        super(
                String.format("User API to path %s seems to be unreachable", fullPath)
        );
    }

}
