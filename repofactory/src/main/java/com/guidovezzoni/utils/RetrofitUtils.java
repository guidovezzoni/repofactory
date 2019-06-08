package com.guidovezzoni.utils;

import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import retrofit2.Response;

import java.io.IOException;

public final class RetrofitUtils {
    private static final String NETWORK_ERROR = "Network error";

    private RetrofitUtils() {
    }

    public static <T> SingleTransformer<Response<T>, T> unWrapResponse() {
        return single -> single.flatMap(response -> {
            if (response.body() != null) {
                return Single.just(response.body());
            }

            try {
                return Single.error(
                        new Exception(
                                response.errorBody() != null ? response.errorBody().string() : NETWORK_ERROR));
            } catch (IOException e) {
                return Single.error(e);
            }
        });
    }
}
