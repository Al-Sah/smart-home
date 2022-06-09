package org.smarthome.controlpanel;


record HistoryRequest(String type, String id, Long from, Long to) {}