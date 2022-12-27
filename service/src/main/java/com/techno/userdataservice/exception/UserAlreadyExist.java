package com.techno.userdataservice.exception;

public class UserAlreadyExist extends Exception {
    private String message;
    public UserAlreadyExist(String message){
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
