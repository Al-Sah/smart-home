package org.smarthome.sdk.models;

import java.util.Date;

/**
 * Send command to the device
 *
 * @author  Al-Sah
 */
public class Command {

    private String hub;
    private String device;
    private String task;
    private long expiration;

    /**
     * @param hub hub uuid
     * @param device sensor/actuator uuid
     * @param task data which be processed by specified actuator
     * @param expiration task will be ignored after expiration time (since Unix Epoch); Set 0 to cancel expiration
     * @throws IllegalArgumentException expiration time in past
     */
    public Command(String hub, String device, String task, long expiration) throws IllegalArgumentException {
        this.hub = hub;
        this.device = device;
        this.task = task;
        if(expiration != 0 && System.currentTimeMillis() > expiration ){
            throw new IllegalArgumentException("Invalid expiration time");
        }
        this.expiration = expiration;
    }


    /**
     *
     * @param device actuator uuid
     * @param task data which be processed by specified actuator
     * @param expiration task will be ignored after expiration time (since Unix Epoch); Set 0 to cancel expiration
     * @throws IllegalArgumentException expiration time in past
     */
    public Command(String hub, String device, String task, Date expiration) throws IllegalArgumentException{
        this(hub, device, task, expiration.getTime());
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

    public void setHub(String hub) {
        this.hub = hub;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
}
