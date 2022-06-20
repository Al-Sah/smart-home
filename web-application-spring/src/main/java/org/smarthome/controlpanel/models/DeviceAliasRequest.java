package org.smarthome.controlpanel.models;

import lombok.NonNull;

public record DeviceAliasRequest(@NonNull String device, @NonNull String alias) { }
