package com.codesight.codesight_api.infrastructure.exception_handling.exceptions.shared;

public class IdCannotBeChangedException extends RuntimeException{

    public IdCannotBeChangedException(){
        super("Id cannot be changed.");
    }
}
