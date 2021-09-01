package com.codesight.codesight_api.infrastructure.exception_handling.exceptions.users;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(int id){
        super("User with id " + id + " does not exist.");
    }
}
