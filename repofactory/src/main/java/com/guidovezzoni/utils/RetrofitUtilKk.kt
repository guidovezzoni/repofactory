package com.guidovezzoni.utils

import io.reactivex.Single
import io.reactivex.SingleTransformer
import retrofit2.Response
import java.io.IOException

class RetrofitUtilKk {
    companion object {
        private const val NETWORK_ERROR = "Network error"

        fun <T> unwrap(): SingleTransformer<Response<T>, T> {
            return SingleTransformer { upstream ->
                upstream.flatMap { response ->
                    response.body().let { Single.just(response.body()) }
                    try {
                        val message = if (response.errorBody() != null) response.errorBody().string() else NETWORK_ERROR
                        return@flatMap Single.error<T>(Exception(message))
                    } catch (e: IOException) {
                        return@flatMap Single.error<T>(e)
                    }
                }
            }
        }
    }
}