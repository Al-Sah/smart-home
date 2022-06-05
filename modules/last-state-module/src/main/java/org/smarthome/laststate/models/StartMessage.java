package org.smarthome.laststate.models;

import lombok.Value;
import org.smarthome.sdk.models.DeviceMessage;
import org.smarthome.sdk.models.DeviceMetadata;
import java.util.List;

@Value public class StartMessage {

    List<DeviceMetadata> devices;
    List<DeviceMessage> errors;
    List<DeviceStateDTO> devicesState;
    List<HubStateDTO> hubsState;
}
