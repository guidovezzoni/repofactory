package com.guidovezzoni.architecture.repository

import io.reactivex.Single

/**
 * Provides an Interface for accessing values provided by the repository pattern
 *
 * @param M data model type
 * @param P parameters type
 */
interface Repository<M, P> {
    /**
     * returns the data from cache (if valid) or from the original source
     *
     * @param params parameters required for the request
     * @return a [Single] that emits the requested value
     */
    fun get(params: P?): Single<M>

    /**
     * same as [get] but with params null
     */
    fun get(): Single<M>

    /**
     * always returns the latest original source, no cache involved
     *
     * @param params parameters required for the request
     * @return a [Single] that emits the requested value
     */
    fun getLatest(params: P?): Single<M>

    /**
     * same as [getLatest] but with params null
     */
    fun getLatest(): Single<M>
}