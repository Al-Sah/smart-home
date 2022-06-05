package org.smarthome.laststate.entities;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <p>Describes state of device</p>
 * Contains information about last changes. All timestamps (lastX) - time in milliseconds since unix epoch
 *
 * @author Al-Sah
 * @see org.smarthome.laststate.entities.HubState
 * @see org.smarthome.laststate.models.DeviceStateDTO
 * @see org.smarthome.laststate.models.ModuleMessage
 */
@Data
@Document(collection="devices")
public class DeviceState {

    /**
     * Device identifier
     */
    @Id
    private final String id;

    /**
     * Hub identifier.
     * Each device is associated with a hub. (device belongs hub)
     */
    private final String owner;

    /**
     * Shows if the device is active or not. <br>
     * Device can be inactive (active = false) when connection with
     * hub is lost or 'device-disconnected' message was received
     */
    private Boolean active;

    /**
     * Timestamp of the last registered 'device-connected' message
     */
    private Long lastConnection;

    /**
     * Timestamp of the last registered 'device-disconnected' message
     */
    private Long lastDisconnection;

    /**
     * <p>On any device message this field updates</p>
     * There are 3 cases when this field updates:
     * <ul>
     *     <li> Device produces some message (error/info) </li>
     *     <li> 'device-connected' message was received </li>
     *     <li> 'device-disconnected' message was received </li>
     * </ul>
     */
    private Long lastUpdate;

    /**
     * This constructor is used by Spring to create @{code DeviceState} using json taken from mongodb
     */
    @PersistenceCreator
    public DeviceState(String id, String owner, Boolean active, Long lastConnection, Long lastDisconnection, Long lastUpdate) {
        this.owner = owner;
        this.id = id;
        this.active = active;
        this.lastConnection = lastConnection;
        this.lastDisconnection = lastDisconnection;
        this.lastUpdate = lastUpdate;
    }

    /**
     * Creates new object with required fields
     * @param id device identifier
     * @param owner hub identifier
     */
    public DeviceState(String id, String owner) {
        this.id = id;
        this.owner = owner;
        this.active = null;
        this.lastConnection = null;
        this.lastDisconnection = null;
        this.lastUpdate = null;
    }
}
