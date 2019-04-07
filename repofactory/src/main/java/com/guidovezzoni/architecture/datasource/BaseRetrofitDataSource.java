package com.guidovezzoni.architecture.datasource;


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

    /**
     * this need to be implemented for the specific network call
     *
     * @param params parameters required for obtaining the appropriate data
     * @return retrofit response
     */
    protected abstract Single<Response<M>> getFromEndPoint(P params);

    @NotNull
    @Override
    public Maybe<TimeStampedData<M>> get(P params) {
        return getFromEndPoint(params)
                .compose(RxUtils.unWrapResponse())
                .map(TimeStampedData.Companion::of)
                .toMaybe();
    }

    @NotNull
    @Override
    public Maybe<TimeStampedData<M>> getAndUpdate(P params, @NotNull DataSource<M, P> cacheSource) {
        return get(params)
                .doAfterSuccess(cacheSource::set);
    }

    @Override
    public void set(@NotNull TimeStampedData<M> model) {
        //TODO complete
    }
}
