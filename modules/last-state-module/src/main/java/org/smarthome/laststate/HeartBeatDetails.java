package org.smarthome.laststate;

public class HeartBeatDetails {

    private final Long HeatBeatPeriod; // in ms
    private Long lastHeatBeat; // in ms
    private Long nextHeatBeat; // in ms  (with latency)


    public HeartBeatDetails(Long heatBeatPeriod, Long lastHeatBeat, Long nextHeatBeat) {
        HeatBeatPeriod = heatBeatPeriod;
        this.lastHeatBeat = lastHeatBeat;
        this.nextHeatBeat = nextHeatBeat;
    }


    public Long getHeatBeatPeriod() {
        return HeatBeatPeriod;
    }

    public Long getLastHeatBeat() {
        return lastHeatBeat;
    }

    public void setLastHeatBeat(Long lastHeatBeat) {
        this.lastHeatBeat = lastHeatBeat;
    }

    public Long getNextHeatBeat() {
        return nextHeatBeat;
    }

    public void setNextHeatBeat(Long nextHeatBeat) {
        this.nextHeatBeat = nextHeatBeat;
    }

}
