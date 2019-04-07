package com.guidovezzoni.architecture.datasource

import com.guidovezzoni.model.TimeStampedData
import io.reactivex.Maybe


/**
 * Provides an interface used to source data. It can be used for network or cache
 *
 * @param M data model
 * @param P parameters required for obtaining the appropriate data
 */
interface DataSource<M, P> {
    fun get(params: P?): Maybe<TimeStampedData<M>>

    /**
     * this is supposed to be called when we need to updated a cache
     *
     * @param params      parameters required for obtaining the appropriate data
     * @param cacheSource a [DataSource] representing the cache that needs updating
     * @return the requested data model
     */
    fun getAndUpdate(params: P?, cacheSource: DataSource<M, P>): Maybe<TimeStampedData<M>>

    fun set(model: TimeStampedData<M>)
}
