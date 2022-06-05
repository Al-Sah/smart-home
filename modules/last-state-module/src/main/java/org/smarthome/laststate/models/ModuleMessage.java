package org.smarthome.laststate.models;

public class ModuleMessage<T> {

    private final ModuleMessageAction action;

    private final T data;

    public ModuleMessage(ModuleMessageAction action, T data) {
        this.action = action;
        this.data = data;
    }

    public ModuleMessageAction getAction() {
        return action;
    }

    public T getData() {
        return data;
    }
}
