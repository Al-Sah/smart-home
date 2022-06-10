package org.smarthome.controlpanel.dtos;

import lombok.Getter;
import org.smarthome.controlpanel.entities.DeviceAlias;

@Getter
public class DeviceAliasDTO {

    private final String user;
    private final String device;
    private final String alias;

    public DeviceAliasDTO(String user, String device, String alias){
        this.user = user;
        this.device = device;
        this.alias = alias;
    }

    public DeviceAliasDTO(DeviceAlias entity, String user){
        this.user = user;
        this.device = entity.getDevice();
        this.alias = entity.getName();
    }
}
