package org.smarthome.climate;

import org.smarthome.sdk.hub.consumer.CommandsHandler;
import org.smarthome.sdk.hub.device.Device;
import org.smarthome.sdk.hub.producer.HubProducer;
import org.smarthome.sdk.hub.producer.HubProducerException;
import org.smarthome.sdk.models.Command;
import org.smarthome.sdk.models.MessageAction;

import java.util.ArrayList;
import java.util.Objects;

public class ClimateHubCommandsHandler implements CommandsHandler {


    private ArrayList<Device> devices;
    private final HubProducer producer;


    public ClimateHubCommandsHandler(HubProducer producer) {
        this.producer = producer;
    }


    public ArrayList<Device> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }

    @Override
    public void handleCommand(Command command) {

        for (Device device : devices) {
            if(Objects.equals(device.getId(), command.getDevice())){
                device.execute(command);
                return;
            }
        }

        try {
            producer.send(MessageAction.HUB_MESSAGE, String.format("device '%s' not found", command.getDevice()), null);
        } catch (HubProducerException e) {
            System.out.println(e.getMessage());
        }
    }

}
