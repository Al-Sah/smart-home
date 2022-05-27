package org.smarthome.climate;

import java.util.Random;

public class ImitationPattern {

    private final int minTime;
    private final int timeDifference;

    private final int maxDifference;

    private final Random random = new Random();

    public ImitationPattern(int minTime, int maxTime, int maxDifference) {
        this.minTime = minTime;
        this.maxDifference = maxDifference;
        this.timeDifference = maxTime - minTime;
    }

    public int newTime(){
        return minTime + random.nextInt(timeDifference);
    }

    public int getMaxDifference() {
        return maxDifference;
    }

}
