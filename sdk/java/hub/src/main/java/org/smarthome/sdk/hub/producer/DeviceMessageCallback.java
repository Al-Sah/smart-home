package org.smarthome.sdk.hub.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.sdk.models.MessageAction;
import org.smarthome.sdk.models.DeviceMessage;

public class DeviceMessageCallback implements DeviceCallback {

    private final HubProducer producer;

    private static final Logger logger = LoggerFactory.getLogger(DeviceMessageCallback.class);

    public DeviceMessageCallback(HubProducer producer) {
        this.producer = producer;
    }

    @Override
    public void send(String device, String component, String property, String value, String error) {

        var sb = new StringBuilder();
        if(device == null || device.isBlank()){
            sb.append("\nfield 'device' is null or blank");
        }

        if(error == null){
            if(component == null || component.isBlank()){
                sb.append("\nfield 'component' is null or blank");
            }
            if(property == null || property.isBlank()){
                sb.append("\nfield 'property' is null or blank");
            }
            if(value == null){
                sb.append("\nfield 'value' is null");
            }
        }

        var result = sb.toString();
        if(!result.isEmpty()){
            logger.warn("Invalid parameters; errors: \n" + result);
            return;
        }
        try {
            producer.send(MessageAction.DEVICE_MESSAGE, new DeviceMessage(device, component, property, value, error));
        } catch (HubProducerException e){
            logger.warn(e.getMessage());
        }
    }
}
