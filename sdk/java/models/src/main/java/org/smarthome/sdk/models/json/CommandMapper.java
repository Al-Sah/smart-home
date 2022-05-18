package org.smarthome.sdk.models.json;

import org.smarthome.sdk.models.Command;


public class CommandMapper {

    public static Command getCommand(JsonCommand command){
        return new Command(
                command.getActuator(),
                command.getTask(),
                command.getExpiration()
        );
    }

    public static JsonCommand getCommand(Command command){
        return new JsonCommand(command);
    }
}
