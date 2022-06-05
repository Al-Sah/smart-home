package org.smarthome.laststate.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <p>Describes state of hub</p>
 * Contains information about last changes. All timestamps (lastX) - time in milliseconds since unix epoch
 *
 * @author Al-Sah
 * @see org.smarthome.laststate.entities.DeviceState
 * @see org.smarthome.laststate.models.HubStateDTO
 * @see org.smarthome.laststate.models.ModuleMessage
 */
@Data
@Document(collection="hubs")
public class HubState {

    /**
     * Hub identifier
     */
    @Id
    private final String id;

    /**
     * Shows if the hub is active or not. <br>
     * Hub can be inactive (active = false) when 'hub-shutdown' message was
     * received or 'heartbeat' message was not received in the expected time
     */
    private Boolean active;

    /**
     * Timestamp of the last registered 'hub-start' message
     */
    private Long lastConnection;

    /**
     * Timestamp of the last registered 'hub-shutdown' message
     */
    private Long lastDisconnection;

    /**
     * <p>On any hub message this field updates</p>
     * There are 3 cases when this field updates:
     * <ul>
     *     <li> 'hub-start' message was received </li>
     *     <li> 'hub-message' message was received </li>
     *     <li> 'hub-shutdown' message was received </li>
     * </ul>
     */
    private Long lastUpdate;

    /**
     * Last received message from hub
     */
    private String lastMessage;

    /**
     * This constructor is used by Spring to create @{code DeviceState} using json taken from mongodb
     */
    @PersistenceCreator
    public HubState(String id, Boolean active, Long lastConnection, Long lastDisconnection, Long lastUpdate, String lastMessage) {
        this.id = id;
        this.active = active;
        this.lastConnection = lastConnection;
        this.lastDisconnection = lastDisconnection;
        this.lastUpdate = lastUpdate;
        this.lastMessage = lastMessage;
    }

    /**
     * Creates new object with required field
     * @param id hub identifier
     */
    public HubState(String id) {
        this.id = id;
        this.active = null;
        this.lastConnection = null;
        this.lastDisconnection = null;
        this.lastUpdate = null;
        this.lastMessage = null;
    }
}
