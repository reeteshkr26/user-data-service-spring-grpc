package com.techno.userdataservice.exception;

public class UserNotFoundException extends Exception{
    private String message;

    public UserNotFoundException(String msg){
        super(msg);
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
