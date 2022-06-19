package org.smarthome.climate;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ImitationPattern {

    private final int minTime;
    private final int timeDifference;
    private final float maxValueDifference;
    private final TimeUnit unit;

    private final Random random = new Random();

    public ImitationPattern(TimeUnit unit, int minTime, int maxTime, float maxDifference) {
        this.unit = unit;
        this.minTime = minTime;
        this.maxValueDifference = maxDifference;
        this.timeDifference = maxTime - minTime;
    }

    public ImitationPattern(TimeUnit unit, int minTime, int maxTime) {
        this(unit, minTime, maxTime, 0);
    }

    public ImitationPattern(int minTime, int maxTime) {
        this(TimeUnit.SECONDS, minTime, maxTime, 0);
    }

    public long newTime(){
        return TimeUnit.MILLISECONDS.convert(minTime + random.nextInt(timeDifference), unit);
    }

    public float getMaxValueDifference() {
        return maxValueDifference;
    }

}
