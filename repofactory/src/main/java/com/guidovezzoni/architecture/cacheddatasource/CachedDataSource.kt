package com.guidovezzoni.architecture.cacheddatasource

import com.guidovezzoni.architecture.datasource.DataSource
import com.guidovezzoni.model.TimeStampedData

/**
 * Interface to handle a cache and it's validity
 **
 * @param M data model type
 * @param P parameters type. This class should override [Object.equals] and [Object.hashCode] as caching uses HashMap.
 * If multiple parameters are required, then [Pair] or [Triple] can be used, or any other Tuple, as long as
 * [Object.equals] and [Object.hashCode] have been properly overridden.
 */
interface CachedDataSource<M, P> : DataSource<M, P> {

    /**
     * caches data
     *
     * @param params parameters required for the request and for caching its response- see above for additional details
     * @param model a value wrapped in [TimeStampedData]
     */
    fun set(params: P?, model: TimeStampedData<M>)

    fun invalidateCache()

    /**
     * @param validity in ms
     */
    fun setCacheValidity(validity: Long)
}