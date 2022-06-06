package org.smarthome.laststate;

public class HeartBeatDetails {

    public static final Integer HEART_BEAT_MAX_LATENCY = 2000;
    private final Long period; // in ms
    private Long lastHeatBeat; // in ms
    private Long nextHeatBeat; // in ms  (including latency)

    private boolean isLost;


    public HeartBeatDetails(Long heatBeatPeriod, Long lastHeatBeat) {
        period = heatBeatPeriod;
        isLost = false;
        moveToNextPeriod(lastHeatBeat);
    }


    public Long getPeriod() {
        return period;
    }

    public Long getLastHeatBeat() {
        return lastHeatBeat;
    }

    public Long getNextHeatBeat() {
        return nextHeatBeat;
    }

    public void moveToNextPeriod(Long last) {
        this.lastHeatBeat = last;
        this.nextHeatBeat = last + period + HEART_BEAT_MAX_LATENCY;
    }


    public boolean isLost() {
        return isLost;
    }

    public void setLost(boolean lost) {
        isLost = lost;
    }

}
