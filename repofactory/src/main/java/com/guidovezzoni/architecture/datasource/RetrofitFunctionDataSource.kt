package com.guidovezzoni.architecture.datasource

import com.guidovezzoni.architecture.cache.TimeStampHelper
import io.reactivex.Single
import retrofit2.Response

open class RetrofitFunctionDataSource<M, P>(
    timeStampHelper: TimeStampHelper, private val endPointGet: Function1<P?, Single<Response<M>>>
) : BaseRetrofitDataSource<M, P>(timeStampHelper) {

    public override fun getFromEndPoint(params: P?): Single<Response<M>> {
        return endPointGet.invoke(params)
    }
}
