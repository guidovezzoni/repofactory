package com.guidovezzoni.architecture.cache;

import com.guidovezzoni.model.TimeStampedData;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;

/**
 * Helper required for testing purposes
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class TimeStampHelper {
    private final Function0<Long> getCurrentTimeStampFunction;

    public TimeStampHelper(@NotNull Function0<Long> getCurrentTimeStampFunction) {
        this.getCurrentTimeStampFunction = getCurrentTimeStampFunction;
    }

    public long getCurrentTimeStamp(){
        return getCurrentTimeStampFunction.invoke();
    }

    public boolean isCacheValid(long cacheTimestamp, long cacheValidity) {
        return checkTimeStampValidity(getCurrentTimeStamp(), cacheTimestamp, cacheValidity);
    }

    public boolean isCacheValid(TimeStampedData timeStampedData, long cacheValidity) {
        return isCacheValid(timeStampedData.getTimestamp(), cacheValidity);
    }

    public static boolean checkTimeStampValidity(long currentTimeStamp, long timeStamp, long cacheValidity) {
        return currentTimeStamp - timeStamp <= cacheValidity;
    }
}
