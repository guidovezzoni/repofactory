package com.guidovezzoni.architecture.cacheddatasource;

import com.guidovezzoni.architecture.cache.TimeStampHelper;
import com.guidovezzoni.model.TimeStampedData;
import io.reactivex.Maybe;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MemoryCacheDataSource<M, P> implements CachedDataSource<M, P> {
    private static final long DEFAULT_CACHE_VALIDITY = TimeUnit.MINUTES.toMillis(5);

    private Map<P, TimeStampedData<M>> cacheMap = new HashMap<>();

    private final TimeStampHelper timeStampHelper;
    private long cacheValidity;

    public MemoryCacheDataSource(@NotNull TimeStampHelper timeStampHelper) {
        this.timeStampHelper = timeStampHelper;
        cacheValidity = DEFAULT_CACHE_VALIDITY;
        invalidateCache();
    }

    /**
     * @implNote params is currently ignored
     */
    @NotNull
    @Override
    public Maybe<TimeStampedData<M>> get(P params) {
        final TimeStampedData<M> cacheValue = getCacheValue(params);
        return cacheValue != null ? Maybe.just(cacheValue) : Maybe.empty();
    }

    /**
     * @implNote params is currently ignored
     */
    @NotNull
    @Override
    public Maybe<TimeStampedData<M>> getAndUpdate(P params, @NotNull CachedDataSource<M, P> cacheSource) {
        final TimeStampedData<M> cacheValue = getCacheValue(params);

        if (cacheValue != null) {
            cacheSource.set(params, cacheValue);
            return Maybe.just(cacheValue);
        } else {
            return Maybe.empty();
        }
    }

    @Override
    public void set(P params, @NotNull TimeStampedData<M> model) {
        setCacheValue(params, model);
    }

    @Override
    public void setCacheValidity(long validity) {
        cacheValidity = validity;
    }

    @Override
    public void invalidateCache() {
        cacheMap.clear();
    }

    private TimeStampedData<M> getCacheValue(P param) {
        final TimeStampedData<M> timeStampedData = cacheMap.get(param);
        return (timeStampedData != null && timeStampHelper.isCacheValid(timeStampedData, cacheValidity))
                ? timeStampedData : null;
    }

    private void setCacheValue(P param, TimeStampedData<M> newValue) {
        if (timeStampHelper.isCacheValid(newValue, cacheValidity)) {
            cacheMap.put(param, newValue);
        }
    }
}
