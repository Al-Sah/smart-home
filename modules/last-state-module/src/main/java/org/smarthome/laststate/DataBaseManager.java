package org.smarthome.laststate;

import org.smarthome.laststate.repositories.DevicesErrorsRepository;
import org.smarthome.laststate.repositories.DevicesRepository;
import org.smarthome.sdk.models.ComponentMetadata;
import org.smarthome.sdk.models.DeviceMessage;
import org.smarthome.sdk.models.DeviceMetadata;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

@Service
public class DataBaseManager {

    private final DevicesRepository devicesRepository;
    private final DevicesErrorsRepository devicesErrorsRepository;

    public DataBaseManager(DevicesRepository devicesRepository, DevicesErrorsRepository devicesErrorsRepository) {
        this.devicesRepository = devicesRepository;
        this.devicesErrorsRepository = devicesErrorsRepository;
    }

    public void saveDevice(DeviceMetadata metadata){
        if(metadata == null){
            return;
        }
        devicesRepository.save(metadata);
    }

    public void saveDeviceError(DeviceMessage message){
        if(message == null){
            return;
        }
        devicesErrorsRepository.save(message);
    }

    public void updateDevice(DeviceMessage message){

        var device =  devicesRepository.findById(message.getDevice()).orElseThrow(
                ()-> new RuntimeException(String.format("Device '%s' not found", message.getDevice()))
        );
        var component = Arrays.stream(device.getComponents())
                .filter(c -> Objects.equals(c.getId(), message.getComponent()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format(
                        "Device '%s' does not contain component '%s'",
                        device.getId(),
                        message.getComponent()))
                );

        if(updateProperty(message, component)){
            devicesRepository.save(device);
        } else {
            throw new RuntimeException(String.format("Property '%s' not found", message.getProperty()));
        }
    }




    private boolean updateProperty(DeviceMessage message, ComponentMetadata component){

        if(Objects.equals(component.getMainProperty().getName(), message.getProperty())){
            component.getMainProperty().setValue(message.getValue());
            return true;
        }

        for (var property : component.getWritableProperties()) {
            if(Objects.equals(property.getName(), message.getProperty())){
                property.setValue(message.getValue());
                return true;
            }
        }
        return false;
    }
}
