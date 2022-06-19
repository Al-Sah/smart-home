package org.smarthome.climate.devices;

public class ThermometerSettings {

    private final TemperatureUnit unit;
    private final int minTemperature;
    private final int maxTemperature;
    private final String name;
    private final float initialSensitivity;

    public ThermometerSettings(String name, TemperatureUnit unit, int minTemperature, int maxTemperature, float sensitivity) {
        this.unit = unit;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.name = name;
        this.initialSensitivity = sensitivity;
    }

    public TemperatureUnit getUnit() {
        return unit;
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public float getInitialSensitivity() {
        return initialSensitivity;
    }

    public String getName() {
        return name;
    }
}
