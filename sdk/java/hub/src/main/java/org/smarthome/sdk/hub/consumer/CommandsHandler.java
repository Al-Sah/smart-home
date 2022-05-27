package org.smarthome.sdk.hub.consumer;

import org.smarthome.sdk.models.Command;

public interface CommandsHandler {

    void handleCommand(Command command);

}
