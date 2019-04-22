package com.guidovezzoni.architecture.cacheddatasource

import com.guidovezzoni.architecture.datasource.DataSource
import com.guidovezzoni.model.TimeStampedData

/**
 * Interface to handle a cache and it's validity
 **
 * @param M data model type
 * @param P parameters type
 */
interface CachedDataSource<M, P> : DataSource<M, P> {

    /**
     * caches data
     *
     * @param model a value wrapped in [TimeStampedData]
     */
    fun set(params: P?, model: TimeStampedData<M>)

    fun invalidateCache()

    /**
     * @param validity in ms
     */
    fun setCacheValidity(validity: Long)
}