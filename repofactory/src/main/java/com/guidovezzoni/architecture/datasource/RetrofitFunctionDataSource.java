package com.guidovezzoni.architecture.datasource;

import com.fernandocejas.arrow.checks.Preconditions;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import kotlin.jvm.functions.Function1;
import retrofit2.Response;

public class RetrofitFunctionDataSource<M, P> extends BaseRetrofitDataSource<M, P> {
    @NonNull
    private final Function1<P, Single<Response<M>>> endPointGet;

    public RetrofitFunctionDataSource(@NonNull Function1<P, Single<Response<M>>> endPointGet) {
        Preconditions.checkNotNull(endPointGet);
        this.endPointGet = endPointGet;
    }

    @Override
    protected Single<Response<M>> getFromEndPoint(P params) {
        return endPointGet.invoke(params);
    }
}
