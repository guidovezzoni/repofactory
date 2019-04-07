package com.guidovezzoni.architecture.cache

/**
 * Adds some method to handle cache
 *
 * Possible additional features:
 * - restore validity
 * - non-expirable cache
 */
interface Cache {
    fun invalidateCache()

    fun setCacheValidity(validity: Long)
}