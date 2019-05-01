package com.guidovezzoni.architecture.datasource

import com.guidovezzoni.architecture.cacheddatasource.CachedDataSource
import com.guidovezzoni.model.TimeStampedData
import io.reactivex.Maybe


/**
 * Provides an interface used to source data. It can be used for "live" sources like network or for cache
 *
 * @param M data model type
 * @param P parameters type. This class should override [Object.equals] and [Object.hashCode] as caching uses HashMap.
 * If multiple parameters are required, then [Pair] or [Triple] can be used, or any other Tuple, as long as
 * [Object.equals] and [Object.hashCode] have been properly overridden.
 */
interface DataSource<M, P> {
    /**
     * returns the data 
     *
     * @param params parameters required for the request and for caching its response- see above for additional details
     * @return a [Maybe] that emits the requested value if present, wrapped in [TimeStampedData]
     */
    fun get(params: P?): Maybe<TimeStampedData<M>>

    /**
     * this is supposed to be called when we need to updated a cache after a successful emission
     *
     * @param params parameters required for the request and for caching its response- see above for additional details
     * @param anotherSource a [CachedDataSource] representing the cache that needs updating
     * @return a [Maybe] that emits the requested value if present, wrapped in [TimeStampedData]
     */
    fun getAndUpdate(params: P?, anotherSource: CachedDataSource<M, P>): Maybe<TimeStampedData<M>>
}
