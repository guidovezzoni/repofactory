package com.guidovezzoni.architecture.datasource

import com.guidovezzoni.model.TimeStampedData
import io.reactivex.Maybe


/**
 * Provides an interface used to source data. It can be used for "live" sources like network or for cache
 *
 * @param M data model type
 * @param P parameters type
 */
interface DataSource<M, P> {
    /**
     * returns the data 
     *
     * @param params parameters required for the request
     * @return a [Maybe] that emits the requested value if present, wrapped in [TimeStampedData]
     */
    fun get(params: P?): Maybe<TimeStampedData<M>>

    /**
     * this is supposed to be called when we need to updated a cache after a successful emission
     *
     * @param params parameters required for the request
     * @param anotherSource a [DataSource] representing the cache that needs updating
     * @return a [Maybe] that emits the requested value if present, wrapped in [TimeStampedData]
     */
    fun getAndUpdate(params: P?, anotherSource: DataSource<M, P>): Maybe<TimeStampedData<M>>

    fun set(model: TimeStampedData<M>)
}
