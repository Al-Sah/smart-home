package org.smarthome.controlpanel.models;

public record HistoryRequest(String type, String id, Long from, Long to) {}