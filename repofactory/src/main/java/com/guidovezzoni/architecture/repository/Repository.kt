package com.guidovezzoni.architecture.repository

import io.reactivex.Single

/**
 * Interface for the repository pattern
 *
 * @param M data model
 * @param P parameters required for obtaining the appropriate
 */
interface Repository<M, P> {
    fun get(params: P?): Single<M>

    /**
     * always get the latest network info, no cache involved
     *
     * @param params parameters required for obtaining the appropriate data
     * @return the data model from the network
     */
    fun getLatest(params: P?): Single<M>
}