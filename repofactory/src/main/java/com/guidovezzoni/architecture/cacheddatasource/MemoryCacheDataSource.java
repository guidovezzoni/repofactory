package com.guidovezzoni.architecture.cacheddatasource;

import com.fernandocejas.arrow.checks.Preconditions;
import com.guidovezzoni.architecture.cache.TimeStampHelper;
import com.guidovezzoni.model.TimeStampedData;
import io.reactivex.Maybe;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class MemoryCacheDataSource<M, P> implements CachedDataSource<M, P> {
    private static final long DEFAULT_CACHE_VALIDITY = TimeUnit.MINUTES.toMillis(5);

    private TimeStampedData<M> cachedValue;

    private final TimeStampHelper timeStampHelper;
    private long cacheValidity;

    public MemoryCacheDataSource(TimeStampHelper timeStampHelper) {
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
        Preconditions.checkArgument(params == null, "Params must be NULL");
        return isCacheValid() ? Maybe.just(cachedValue) : Maybe.empty();
    }

    /**
     * @implNote params is currently ignored
     */
    @NotNull
    @Override
    public Maybe<TimeStampedData<M>> getAndUpdate(P params, @NotNull CachedDataSource<M, P> cacheSource) {
        Preconditions.checkArgument(params == null, "Params must be NULL");
        if (isCacheValid()) {
            cacheSource.set(cachedValue);
            return Maybe.just(cachedValue);
        } else {
            return Maybe.empty();
        }
    }

    @Override
    public void set(@NotNull TimeStampedData<M> model) {
        updateCache(model);
    }

    @Override
    public void setCacheValidity(long validity) {
        cacheValidity = validity;
    }

    @Override
    public void invalidateCache() {
        updateCache(null);
    }

    private void updateCache(TimeStampedData<M> newValue) {
        cachedValue = newValue;
    }

    private boolean isCacheValid() {
        return cachedValue != null && timeStampHelper.isCacheValid(cachedValue.getTimestamp(), cacheValidity);
    }
}
