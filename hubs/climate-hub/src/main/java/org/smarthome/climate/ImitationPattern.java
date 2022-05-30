package org.smarthome.climate;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ImitationPattern {

    private final int minTime;
    private final int timeDifference;

    private final float maxDifference;

    private final TimeUnit unit;

    private final Random random = new Random();

    public ImitationPattern(int minTime, int maxTime, float maxDifference, TimeUnit unit) {
        this.unit = unit;
        this.minTime = minTime;
        this.maxDifference = maxDifference;
        this.timeDifference = maxTime - minTime;
    }

    public int newTime(){
        int newTime = minTime + random.nextInt(timeDifference);
        if(unit == TimeUnit.SECONDS){
            return newTime * 1000;
        }
        if(unit == TimeUnit.MINUTES){
            return newTime * 60000;
        }
        return newTime;
    }

    public float getMaxDifference() {
        return maxDifference;
    }

}
