package org.smarthome.climate;

public enum TemperatureUnit {
    fahrenheit("F"),
    celsius("C");

    public final String name;
    TemperatureUnit(String name) {
        this.name = name;
    }
}
