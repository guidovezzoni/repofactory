package com.guidovezzoni.architecture.repository;


import com.guidovezzoni.architecture.datasource.DataSource;
import com.guidovezzoni.model.TimeStampedData;
import io.reactivex.Single;
import org.jetbrains.annotations.NotNull;

/**
 * Basic version of Repository Pattern, with no cache
 *
 * @param <M> data model
 * @param <P> parameters required for obtaining the appropriate data
 */
@SuppressWarnings("unused")
public class NoCacheRepository<M, P> extends BaseRepository<M, P> implements Repository<M, P> {
    private final DataSource<M, P> networkDataSource;

    public NoCacheRepository(DataSource<M, P> networkDataSource) {
        this.networkDataSource = networkDataSource;
    }

    @NotNull
    @Override
    public Single<M> get(P params) {
        return getLatest(params);
    }

    @NotNull
    @Override
    public Single<M> getLatest(P params) {
        return networkDataSource.get(params)
                .map(TimeStampedData::getModel)
                .toSingle();
    }
}
