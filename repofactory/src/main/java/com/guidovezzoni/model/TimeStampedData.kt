package com.guidovezzoni.model

@Suppress("unused")
data class TimeStampedData<M>(val model: M, val timestamp: Long) {

    companion object {
        fun <T> of(t: T, timestamp: Long): TimeStampedData<T> {
            return TimeStampedData(t, timestamp)
        }
    }
}
