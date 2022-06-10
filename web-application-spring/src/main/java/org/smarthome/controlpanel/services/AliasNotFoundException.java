package org.smarthome.controlpanel.services;

public class AliasNotFoundException extends RuntimeException {

    public AliasNotFoundException(String user, String device) {
        super(String.format("User '%s' do not have alias for the device '%s'", user, device));
    }
}
