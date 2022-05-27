package org.smarthome.module;

import java.util.Date;

/**
 * Send command to the device
 *
 * @author  Al-Sah
 */
public class UserCommand {

    private final String hub;
    private final String device;
    private final String component;
    private final String property;
    private final String options;
    private final long expiration;

    /**
     * @param hub       hub uuid
     * @param device    sensor/actuator uuid
     * @param component device component
     * @param property  device property
     * @param options   additional options
     * @param expiration task will be ignored after expiration time (since Unix Epoch); Set 0 to cancel expiration
     * @throws IllegalArgumentException expiration time in past
     */
    public UserCommand(
            String hub,
            String device,
            String component,
            String property,
            String options,
            long expiration) throws IllegalArgumentException {
        this.hub = hub;
        this.device = device;
        this.component = component;
        this.property = property;
        this.options = options;
        if(expiration != 0 && System.currentTimeMillis() > expiration ){
            throw new IllegalArgumentException("Invalid expiration time");
        }
        this.expiration = expiration;
    }


    /**
     * @param device     actuator uuid
     * @param expiration task will be ignored after expiration time (since Unix Epoch); Set 0 to cancel expiration
     * @param component  device component
     * @param property   device property
     * @throws IllegalArgumentException expiration time in past
     */
    public UserCommand(String hub, String device, Date expiration, String component, String property) throws IllegalArgumentException{
        this(hub, device, component, property, null, expiration.getTime());
    }


    /**
     * Get time in future after specified number of seconds (since Unix Epoch)
     * @param seconds number of seconds
     */
    public static long getFuture(int seconds){
        // Convert seconds to milliseconds
        return System.currentTimeMillis() + (seconds * 60000L);
    }


    public String getHub() {
        return hub;
    }

    public String getDevice() {
        return device;
    }

    public String getComponent() {
        return component;
    }

    public String getProperty() {
        return property;
    }

    public String getOptions() {
        return options;
    }

    public long getExpiration() {
        return expiration;
    }
}
