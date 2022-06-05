package org.smarthome.laststate.models;

import lombok.Getter;

@Getter
public class ModuleMessage<T> {

    private final ModuleMessageAction action;

    private final T data;

    public ModuleMessage(ModuleMessageAction action, T data) {
        this.action = action;
        this.data = data;
    }
}
