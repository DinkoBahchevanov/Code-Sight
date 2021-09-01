package com.codesight.codesight_api.infrastructure.exception_handling.exceptions.users;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String email){
        super("User with the email " + email + " already exists");
    }
}
