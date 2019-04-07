package com.guidovezzoni.architecture.cache;

import kotlin.jvm.functions.Function0;

/**
 * Helper required for testing purposes
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class CacheHelper {
    private final Function0<Long> getCurrentTimeStampFunction;

    public CacheHelper(Function0<Long> getCurrentTimeStampFunction) {
        this.getCurrentTimeStampFunction = getCurrentTimeStampFunction;
    }

    public boolean isTimeStampValid(long timestamp, long cacheValidity) {
        return isTimeStampValid(getCurrentTimeStampFunction.invoke(), timestamp, cacheValidity);
    }

    public boolean isTimeStampValid(long currentTimeStamp, long timeStamp, long cacheValidity) {
        return currentTimeStamp - timeStamp <= cacheValidity;
    }
}
