package org.smarthome.sdk.models;

/**
 * @see ProducerConfiguration
 * @see DeviceData
 * @see HubMessage
 * @see HubMessage.Action
 * @author  Al-Sah
 */
public class InvalidModelParams extends RuntimeException{

    public InvalidModelParams(String message) {
        super(message);
    }
}
