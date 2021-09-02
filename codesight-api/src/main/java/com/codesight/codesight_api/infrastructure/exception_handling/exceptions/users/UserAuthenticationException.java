package com.codesight.codesight_api.infrastructure.exception_handling.exceptions.users;

public class UserAuthenticationException extends RuntimeException {

    public UserAuthenticationException(){
        super("Wrong email or password!");
    }
}
