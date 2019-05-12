package com.guidovezzoni.architecture.datasource;


import com.guidovezzoni.architecture.cache.TimeStampHelper;
import com.guidovezzoni.architecture.cacheddatasource.CachedDataSource;
import com.guidovezzoni.model.TimeStampedData;
import com.guidovezzoni.utils.RxUtils;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.jetbrains.annotations.NotNull;
import retrofit2.Response;

/**
 * This class fetches the info from the retrofit service and onSuccess returns the result
 * on stream - or returns an errors if that is the case
 *
 * @param <M> data model
 * @param <P> parameters required for obtaining the appropriate data
 */
public abstract class BaseRetrofitDataSource<M, P> implements DataSource<M, P> {

    @NotNull
    private final TimeStampHelper timeStampHelper;

    /**
     * this needs to be implemented for a specific network call
     *
     * @param params parameters required for the network request
     * @return retrofit response
     */
    protected abstract Single<Response<M>> getFromEndPoint(P params);

    @SuppressWarnings("WeakerAccess")
    public BaseRetrofitDataSource(@NotNull TimeStampHelper timeStampHelper) {
        this.timeStampHelper = timeStampHelper;
    }

    @NotNull
    @Override
    public Maybe<TimeStampedData<M>> get(P params) {
        return getFromEndPoint(params)
                .compose(RxUtils.unWrapResponse())
                .map((M t) -> TimeStampedData.Companion.of(t, timeStampHelper.getCurrentTimeStamp()))
                .toMaybe();
    }

    @NotNull
    @Override
    public Maybe<TimeStampedData<M>> getAndUpdate(P params, @NotNull CachedDataSource<M, P> cacheSource) {
        return get(params)
                .doAfterSuccess(timeStampedData -> cacheSource.set(params, timeStampedData));
    }
}
