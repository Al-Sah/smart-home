package org.smarthome.laststate.models;

import lombok.NonNull;
import lombok.Value;

@Value public class ModuleMessage<T> {

    @NonNull ModuleMessageAction action;
    @NonNull T data;
}
