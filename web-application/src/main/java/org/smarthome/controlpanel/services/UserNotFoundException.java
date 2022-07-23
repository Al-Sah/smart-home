package org.smarthome.controlpanel.services;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String username) {
        super(String.format("User with '%s' not found", username));
    }
}
