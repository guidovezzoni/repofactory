package com.guidovezzoni.repofactory;

import com.guidovezzoni.architecture.cache.TimeStampHelper;
import com.guidovezzoni.architecture.cacheddatasource.MemoryCacheDataSource;
import com.guidovezzoni.architecture.datasource.DataSource;
import com.guidovezzoni.architecture.datasource.RetrofitFunctionDataSource;
import com.guidovezzoni.architecture.repository.NoCacheRepository;
import com.guidovezzoni.architecture.repository.Repository;
import com.guidovezzoni.architecture.repository.SingleLevelCacheRepository;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import kotlin.jvm.functions.Function1;
import retrofit2.Response;

@SuppressWarnings({"WeakerAccess", "unused"})
public class RepoFactory {
    public final TimeStampHelper timeStampHelper;

    public RepoFactory() {
        timeStampHelper = createTimeStampHelper();
    }

    protected TimeStampHelper createTimeStampHelper() {
        return new TimeStampHelper(System::currentTimeMillis);
    }

    /**
     * Override this method to create a custom {@link DataSource} implementation for a network request.
     *
     * @param endPointGet functional interface that when invoked calls the wanted endpoint
     * @param <M>         type for the endpoint response and cached data
     * @param <P>         parameters type. This class should override {@link Object#equals(Object)} and {@link Object#hashCode()} as caching uses HashMap.
     *                    If multiple parameters are required, then {@link android.util.Pair} or {@link kotlin.Pair} or {@link kotlin.Triple} can be used, or any other Tuple, as long as
     *                    [Object.equals] and [Object.hashCode] have been properly overridden.
     * @return a {@link DataSource} instance
     */
    protected <M, P> DataSource<M, P> createNetworkDataSource(@NonNull Function1<P, Single<Response<M>>> endPointGet) {
        return new RetrofitFunctionDataSource<>(timeStampHelper, endPointGet);
    }

    /**
     * Override this method to create a custom {@link MemoryCacheDataSource} implementation for a cache.
     *
     * @param <M> type for the endpoint response and cached data
     * @param <P> parameters type. This class should override {@link Object#equals(Object)} and {@link Object#hashCode()} as caching uses HashMap.
     *            If multiple parameters are required, then {@link android.util.Pair} or {@link kotlin.Pair} or {@link kotlin.Triple} can be used, or any other Tuple, as long as
     *            [Object.equals] and [Object.hashCode] have been properly overridden.
     * @return a {@link MemoryCacheDataSource} instance
     */
    protected <M, P> MemoryCacheDataSource<M, P> createCachedSource() {
        return new MemoryCacheDataSource<>(timeStampHelper);
    }

    /**
     * Override this method to create a custom {@link Repository} implementation for the repository pattern.
     *
     * @param repoType    select one from the existing repository types
     * @param endPointGet functional interface that when invoked calls the wanted endpoint
     * @param <M>         type for the endpoint response and cached data
     * @param <P>         parameters type. This class should override {@link Object#equals(Object)} and {@link Object#hashCode()} as caching uses HashMap.
     *                    If multiple parameters are required, then {@link android.util.Pair} or {@link kotlin.Pair} or {@link kotlin.Triple} can be used, or any other Tuple, as long as
     *                    [Object.equals] and [Object.hashCode] have been properly overridden.
     * @return a {@link Repository} instance
     */
    public <M, P> Repository<M, P> createRepo(RepoType repoType, @NonNull Function1<P, Single<Response<M>>> endPointGet) {
        DataSource<M, P> networkSource = createNetworkDataSource(endPointGet);

        switch (repoType) {
            case NO_CACHE:
                return new NoCacheRepository<>(networkSource);
            case SINGLE_LEVEL_CACHE:
                MemoryCacheDataSource<M, P> cachedDataSource = createCachedSource();
                return new SingleLevelCacheRepository<>(networkSource, cachedDataSource);
            default:
                throw new IllegalArgumentException("repo type not defined");
        }
    }
}
