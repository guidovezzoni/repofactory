package com.guidovezzoni.architecture.datasource

import com.guidovezzoni.architecture.cache.TimeStampHelper
import com.guidovezzoni.utils.RetrofitUtils
import io.reactivex.Single
import io.reactivex.SingleTransformer
import retrofit2.Response

/**
 * implementation of [BaseRetrofitDataSource] that uses a [Function1] to define [getFromEndPoint]
 */
open class RetrofitFunctionDataSource<M, P> @JvmOverloads constructor(
    timeStampHelper: TimeStampHelper,
    retrofitUnwrapper: SingleTransformer<Response<M>, M> = RetrofitUtils.unWrapResponse(),
    private val endPointGet: Function1<P?, Single<Response<M>>>
) : BaseRetrofitDataSource<M, P>(timeStampHelper, retrofitUnwrapper) {

    public override fun getFromEndPoint(params: P?): Single<Response<M>> {
        return endPointGet.invoke(params)
    }
}
