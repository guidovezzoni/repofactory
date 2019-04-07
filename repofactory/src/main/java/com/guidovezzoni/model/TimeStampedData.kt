package com.guidovezzoni.model

@Suppress("unused")
class TimeStampedData<M>(val model: M, val timestamp: Long) {
    constructor(model: M) : this(model, System.currentTimeMillis())

    companion object {

        fun <T> of(t: T): TimeStampedData<T> {
            return TimeStampedData(t)
        }

        fun <T> of(t: T, timestamp: Long): TimeStampedData<T> {
            return TimeStampedData(t, timestamp)
        }
    }
}
