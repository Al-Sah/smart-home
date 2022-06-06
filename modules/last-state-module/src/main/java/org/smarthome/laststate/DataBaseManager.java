package org.smarthome.laststate;

import org.smarthome.laststate.entities.HubState;
import org.smarthome.laststate.entities.DeviceState;
import org.smarthome.laststate.repositories.DevicesStateRepository;
import org.smarthome.laststate.repositories.DevicesErrorsRepository;
import org.smarthome.laststate.repositories.DevicesRepository;
import org.smarthome.laststate.repositories.HubStateRepository;
import org.smarthome.sdk.models.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
@Service
public class DataBaseManager {

    private final DevicesRepository devicesRepository;
    private final DevicesErrorsRepository devicesErrorsRepository;
    private final DevicesStateRepository devicesStateRepository;
    private final HubStateRepository hubStateRepository;


    public DataBaseManager(
            DevicesRepository devicesRepository,
            DevicesErrorsRepository devicesErrorsRepository,
            DevicesStateRepository devicesStateRepository,
            HubStateRepository hubStateRepository) {
        this.devicesRepository = devicesRepository;
        this.devicesErrorsRepository = devicesErrorsRepository;
        this.devicesStateRepository = devicesStateRepository;
        this.hubStateRepository = hubStateRepository;
    }


    public List<DeviceMetadata> getAllDevices(){
        return devicesRepository.findAll();
    }

    public List<DeviceMessage> getAllDevicesErrors(){
        return devicesErrorsRepository.findAll();
    }

    public List<DeviceState> getAllDevicesState(){
        return devicesStateRepository.findAll();
    }

    public List<HubState> getAllHubsState(){
        return hubStateRepository.findAll();
    }


    public DeviceState saveDevice(DeviceMetadata metadata, String hub){
        if(metadata == null){
            throw new RuntimeException("metadata is null");
        }
        devicesRepository.save(metadata);
        var state = devicesStateRepository.findById(metadata.getId())
                .orElse(new DeviceState(metadata.getId(), hub));

        
        state.setActive(true);
        state.setLastUpdate(System.currentTimeMillis());
        state.setLastConnection(System.currentTimeMillis());
        
        devicesStateRepository.save(state);
        return state;
    }

    public DeviceState saveDeviceError(DeviceMessage msg){
        if(msg == null){
            throw new RuntimeException("message is null");
        }
        devicesErrorsRepository.save(msg);
        return updateDeviceState(msg.getDevice());
    }


    public HubState setHubStateConnected(String id){
        var hub =  hubStateRepository.findById(id).orElse(new HubState(id));
        hub.setActive(true);
        hub.setLastConnection(System.currentTimeMillis());
        hub.setLastUpdate(System.currentTimeMillis());
        hubStateRepository.save(hub);
        return hub;
    }

    public HubState setHubStateAlive(String id){
        var hub =  hubStateRepository.findById(id).orElse(new HubState(id));
        hub.setActive(true);
        hub.setLastUpdate(System.currentTimeMillis());
        hubStateRepository.save(hub);
        return hub;
    }

    public HubState updateHubState(String msg, String id){
        var hub = hubStateRepository.findById(id).orElse(new HubState(id));
        hub.setActive(true);
        hub.setLastUpdate(System.currentTimeMillis());
        hub.setLastMessage(msg);
        hubStateRepository.save(hub);
        return hub;
    }

    public HubState setHubStateDisconnected(String id){
        var hub = hubStateRepository.findById(id).orElse(new HubState(id));
        hub.setActive(false);
        hub.setLastUpdate(System.currentTimeMillis());
        hub.setLastDisconnection(System.currentTimeMillis());
        hubStateRepository.save(hub);
        return hub;
    }

    public HubState setHubStateLost(String id){
        var hub = hubStateRepository.findById(id).orElse(new HubState(id));
        hub.setActive(false);
        hubStateRepository.save(hub);
        return hub;
    }

    public List<DeviceState> removeActiveDevices(String hubId){
        var devices = devicesStateRepository.findAllByOwnerAndActive(hubId, true);

        for (DeviceState device : devices) {
            device.setActive(false);
            device.setLastDisconnection(System.currentTimeMillis());
        }
        devicesStateRepository.saveAll(devices);
        return devices;
    }

    public List<DeviceState> updateActiveDevices(String hubId, String[] active){
        var activeDevices = Arrays.asList(active);
        var devices = devicesStateRepository.findAllByOwner(hubId);

        for (DeviceState device : devices) {
            device.setActive(activeDevices.contains(device.getId()));
        }
        devicesStateRepository.saveAll(devices);
        return devices;
    }
    

    public DeviceState updateDevice(DeviceMessage msg){

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



    public DeviceState updateDeviceState(DeviceDisconnectionDetails disconnectionDetails) throws RuntimeException{
        var id= disconnectionDetails.getDeviceId();
        var state = devicesStateRepository.findById(id).orElseThrow(
                ()-> new RuntimeException(String.format("DeviceDeletes '%s' not found", id))
        );
        state.setActive(false);
        state.setLastUpdate(System.currentTimeMillis());
        state.setLastDisconnection(System.currentTimeMillis());
        devicesStateRepository.save(state);
        return state;
    }

    private DeviceState updateDeviceState(String id) throws RuntimeException{

        var state = devicesStateRepository.findById(id).orElseThrow(
                ()-> new RuntimeException(String.format("DeviceDeletes '%s' not found", id))
        );
        state.setActive(true);
        state.setLastUpdate(System.currentTimeMillis());
        devicesStateRepository.save(state);
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
