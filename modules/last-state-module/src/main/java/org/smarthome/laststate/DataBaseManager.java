package org.smarthome.laststate;

import org.smarthome.laststate.entities.HubStateDetails;
import org.smarthome.laststate.entities.DeviceStateDetails;
import org.smarthome.laststate.repositories.DevicesStateDetailsRepository;
import org.smarthome.laststate.repositories.DevicesErrorsRepository;
import org.smarthome.laststate.repositories.DevicesRepository;
import org.smarthome.laststate.repositories.HubStateDetailsRepository;
import org.smarthome.sdk.models.ComponentMetadata;
import org.smarthome.sdk.models.DeviceDisconnectionDetails;
import org.smarthome.sdk.models.DeviceMessage;
import org.smarthome.sdk.models.DeviceMetadata;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class DataBaseManager {

    private final DevicesRepository devicesRepository;
    private final DevicesErrorsRepository devicesErrorsRepository;
    private final DevicesStateDetailsRepository devicesStateDetailsRepository;
    private final HubStateDetailsRepository hubStateDetailsRepository;


    public DataBaseManager(
            DevicesRepository devicesRepository,
            DevicesErrorsRepository devicesErrorsRepository,
            DevicesStateDetailsRepository devicesStateDetailsRepository,
            HubStateDetailsRepository hubStateDetailsRepository) {
        this.devicesRepository = devicesRepository;
        this.devicesErrorsRepository = devicesErrorsRepository;
        this.devicesStateDetailsRepository = devicesStateDetailsRepository;
        this.hubStateDetailsRepository = hubStateDetailsRepository;
    }


    public List<DeviceMetadata> getAllDevices(){
        return devicesRepository.findAll();
    }

    public List<DeviceMessage> getAllDevicesErrors(){
        return devicesErrorsRepository.findAll();
    }

    public List<DeviceStateDetails> getAllDevicesState(){
        return devicesStateDetailsRepository.findAll();
    }

    public List<HubStateDetails> getAllHubsState(){
        return hubStateDetailsRepository.findAll();
    }


    public DeviceStateDetails saveDevice(DeviceMetadata metadata, String hub){
        if(metadata == null){
            throw new RuntimeException("metadata is null");
        }
        devicesRepository.save(metadata);
        var state = devicesStateDetailsRepository.findById(metadata.getId()).orElse(null);

        if(state == null){
            state = new DeviceStateDetails(hub, metadata.getId(), true, System.currentTimeMillis(), null, null);
        }else {
            state.setActive(true);
            state.setLastConnection(System.currentTimeMillis());
        }
        devicesStateDetailsRepository.save(state);
        return state;
    }

    public DeviceStateDetails saveDeviceError(DeviceMessage msg){
        if(msg == null){
            throw new RuntimeException("message s null");
        }
        devicesErrorsRepository.save(msg);
        return updateDeviceState(msg.getDevice());
    }

    public DeviceStateDetails updateDevice(DeviceMessage msg){

        var device =  devicesRepository.findById(msg.getDevice()).orElseThrow(
                ()-> new RuntimeException(String.format("Device '%s' not found", msg.getDevice()))
        );
        var component = Arrays.stream(device.getComponents())
                .filter(c -> Objects.equals(c.getId(), msg.getComponent()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format(
                        "Device '%s' does not contain component '%s'",
                        device.getId(),
                        msg.getComponent()))
                );

        if(updateProperty(msg, component)){
            devicesRepository.save(device);
            return updateDeviceState(msg.getDevice());
        } else {
            throw new RuntimeException(String.format("Property '%s' not found", msg.getProperty()));
        }
    }



    public DeviceStateDetails updateDeviceState(DeviceDisconnectionDetails disconnectionDetails) throws RuntimeException{
        var id= disconnectionDetails.getDeviceId();
        var state = devicesStateDetailsRepository.findById(id).orElseThrow(
                ()-> new RuntimeException(String.format("DeviceDeletes '%s' not found", id))
        );
        state.setActive(false);
        state.setLastDisconnection(System.currentTimeMillis());
        devicesStateDetailsRepository.save(state);
        return state;
    }

    private DeviceStateDetails updateDeviceState(String id) throws RuntimeException{

        var state = devicesStateDetailsRepository.findById(id).orElseThrow(
                ()-> new RuntimeException(String.format("DeviceDeletes '%s' not found", id))
        );


        state.setActive(true);
        state.setLastUpdate(System.currentTimeMillis());

        devicesStateDetailsRepository.save(state);
        return state;
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
