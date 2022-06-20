package org.smarthome.controlpanel.models;

import lombok.NonNull;

public record HistoryRequest(@NonNull String type, @NonNull String id, Long from, Long to) {}