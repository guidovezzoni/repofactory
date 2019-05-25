package com.guidovezzoni.architecture.cacheddatasource

import com.guidovezzoni.architecture.cache.TimeStampHelper
import com.guidovezzoni.model.TimeStampedData
import io.reactivex.Maybe
import java.util.concurrent.TimeUnit

open class MemoryCacheDataSource<M, P>(private val timeStampHelper: TimeStampHelper) : CachedDataSource<M, P> {
    private val cacheMap = mutableMapOf<P?, TimeStampedData<M>>()
    private var cacheValidity = DEFAULT_CACHE_VALIDITY

    init {
        invalidateCache()
    }

    override fun set(params: P?, model: TimeStampedData<M>) {
        setCacheValue(params, model)
    }

    override fun invalidateCache() {
        cacheMap.clear()
    }

    override fun setCacheValidity(validity: Long) {
        cacheValidity = validity
    }

    override fun get(params: P?): Maybe<TimeStampedData<M>> {
        val cacheValue = getCacheValue(params)
        return if (cacheValue != null) Maybe.just(cacheValue) else Maybe.empty()
    }

    override fun getAndUpdate(params: P?, anotherSource: CachedDataSource<M, P>): Maybe<TimeStampedData<M>> {
        val cacheValue = getCacheValue(params)

        return if (cacheValue != null) {
            anotherSource.set(params, cacheValue)
            Maybe.just(cacheValue)
        } else {
            Maybe.empty()
        }
    }

    private fun getCacheValue(params: P?): TimeStampedData<M>? {
        return cacheMap[params]?.let { if (timeStampHelper.isCacheValid(it, cacheValidity)) it else null }
    }

    private fun setCacheValue(params: P?, newValue: TimeStampedData<M>) {
        if (timeStampHelper.isCacheValid(newValue, cacheValidity)) cacheMap[params] = newValue
    }

    companion object {
        private val DEFAULT_CACHE_VALIDITY = TimeUnit.MINUTES.toMillis(5)
    }
}
