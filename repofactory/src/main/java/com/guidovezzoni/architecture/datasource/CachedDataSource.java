package com.guidovezzoni.architecture.datasource;

import com.fernandocejas.arrow.checks.Preconditions;
import com.guidovezzoni.architecture.cache.Cache;
import com.guidovezzoni.architecture.cache.CacheHelper;
import com.guidovezzoni.model.TimeStampedData;
import io.reactivex.Maybe;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class CachedDataSource<M, P> implements DataSource<M, P>, Cache {
    private static final long DEFAULT_CACHE_VALIDITY = TimeUnit.MINUTES.toMillis(5);

    private TimeStampedData<M> cachedValue;

    private final CacheHelper cacheHelper;
    private long cacheValidity;

    public CachedDataSource(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
        cacheValidity = DEFAULT_CACHE_VALIDITY;
        invalidateCache();
    }

    @NotNull
    @Override
    public Maybe<TimeStampedData<M>> get(P params) {
        //TODO params is currently ignored
        Preconditions.checkArgument(params == null, "Params must be NULL");
        return isCacheValid() ? Maybe.just(cachedValue) : Maybe.empty();
    }

    @NotNull
    @Override
    public Maybe<TimeStampedData<M>> getAndUpdate(P params, @NotNull DataSource<M, P> cacheSource) {
        //TODO params is currently ignored
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
        return cachedValue != null && cacheHelper.isTimeStampValid(cachedValue.getTimestamp(), cacheValidity);
    }
}
