package com.guidovezzoni.architecture.cache;

import kotlin.jvm.functions.Function0;

/**
 * Helper required for testing purposes
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class TimeStampHelper {
    private final Function0<Long> getCurrentTimeStampFunction;

    public TimeStampHelper(Function0<Long> getCurrentTimeStampFunction) {
        this.getCurrentTimeStampFunction = getCurrentTimeStampFunction;
    }

    public long getCurrentTimeStamp(){
        return getCurrentTimeStampFunction.invoke();
    }

    public boolean isCacheValid(long cacheTimestamp, long cacheValidity) {
        return checkTimeStampValidity(getCurrentTimeStamp(), cacheTimestamp, cacheValidity);
    }

    public static boolean checkTimeStampValidity(long currentTimeStamp, long timeStamp, long cacheValidity) {
        return currentTimeStamp - timeStamp <= cacheValidity;
    }
}
