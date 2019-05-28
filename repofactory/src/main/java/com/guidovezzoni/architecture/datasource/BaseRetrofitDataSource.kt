package com.guidovezzoni.architecture.datasource

import com.guidovezzoni.architecture.cache.TimeStampHelper
import com.guidovezzoni.architecture.cacheddatasource.CachedDataSource
import com.guidovezzoni.model.TimeStampedData
import com.guidovezzoni.utils.RxUtils
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.Response

/**
 * This class fetches the info from the retrofit service and onSuccess returns the result
 * on stream - or returns an errors if that is the case
 *
 * @param M data model
 * @param P parameters required for obtaining the appropriate data
 */
abstract class BaseRetrofitDataSource<M, P>(private val timeStampHelper: TimeStampHelper) : DataSource<M, P> {

    /**
     * this needs to be implemented for a specific network call
     *
     * @param params parameters required for the network request
     * @return retrofit response
     */
    protected abstract fun getFromEndPoint(params: P?): Single<Response<M>>

    override fun get(params: P?): Maybe<TimeStampedData<M>> {
        return getFromEndPoint(params)
            .compose(RxUtils.unWrapResponse())
            .map { t: M -> TimeStampedData.of(t, timeStampHelper.currentTimeStamp) }
            .toMaybe()
    }

    override fun getAndUpdate(params: P?, anotherSource: CachedDataSource<M, P>): Maybe<TimeStampedData<M>> {
        return get(params)
            .doAfterSuccess { timeStampedData -> anotherSource.set(params, timeStampedData) }
    }
}
