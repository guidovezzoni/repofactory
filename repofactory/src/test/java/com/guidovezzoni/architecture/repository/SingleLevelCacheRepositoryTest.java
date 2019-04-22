package com.guidovezzoni.architecture.repository;

import com.guidovezzoni.architecture.cacheddatasource.MemoryCacheDataSource;
import com.guidovezzoni.architecture.datasource.DataSource;
import com.guidovezzoni.model.TimeStampedData;
import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class SingleLevelCacheRepositoryTest {
    private static final String CACHE_STRING = "Cache";
    private static final Long TIMESTAMP = 47L;
    private static final TimeStampedData<String> CACHE_DATA = TimeStampedData.Companion.of(CACHE_STRING, TIMESTAMP);

    private static final String NETWORK_STRING = "Network";
    private static final TimeStampedData<String> NETWORK_DATA = TimeStampedData.Companion.of(NETWORK_STRING, TIMESTAMP);

    @Mock
    private DataSource<String, Double> networkDataSource;
    @Mock
    private MemoryCacheDataSource<String, Double> cacheDataSource;

    private SingleLevelCacheRepository<String, Double> sut;

    @Parameterized.Parameters
    public static Iterable<Object> data() {
        return Arrays.asList(27.48, 5.0, null, new Random().nextDouble());
    }

    @Parameterized.Parameter
    public Double parameter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sut = new SingleLevelCacheRepository<>(networkDataSource, cacheDataSource);
    }

    @Test
    public void whenGetWithCacheAvailableThenReturnCache() {
        TestObserver<String> testObserver = TestObserver.create();
        when(cacheDataSource.get(parameter)).thenReturn(Maybe.just(CACHE_DATA));
        when(networkDataSource.getAndUpdate(parameter, cacheDataSource)).thenReturn(Maybe.just(NETWORK_DATA));

        sut.get(parameter)
                .subscribe(testObserver);

        testObserver.assertResult(CACHE_STRING);  // includes .assertComplete().assertNoErrors()
        verify(cacheDataSource).get(parameter);
        verify(networkDataSource).getAndUpdate(parameter, cacheDataSource);
    }

    @Test
    public void whenGetWithCacheNotAvailableThenReturnFromNetwork() {
        TestObserver<String> testObserver = TestObserver.create();
        when(cacheDataSource.get(parameter)).thenReturn(Maybe.empty());
        when(networkDataSource.getAndUpdate(parameter, cacheDataSource)).thenReturn(Maybe.just(NETWORK_DATA));

        sut.get(parameter)
                .subscribe(testObserver);

        testObserver.assertResult(NETWORK_STRING); // includes .assertComplete().assertNoErrors()
        verify(cacheDataSource).get(parameter);
        verify(cacheDataSource, never()).set(anyDouble(), any());
        verify(networkDataSource).getAndUpdate(parameter, cacheDataSource);
    }

    @Test
    public void whenGetWithNetworkFailureThenHandleIt() {
        TestObserver<String> testObserver = TestObserver.create();
        when(cacheDataSource.get(parameter)).thenReturn(Maybe.empty());
        when(networkDataSource.getAndUpdate(parameter, cacheDataSource)).thenReturn(Maybe.error(new Exception("generic network error")));

        sut.get(parameter)
                .subscribe(testObserver);

        testObserver.assertNotComplete()
                .assertError(throwable -> throwable instanceof Exception)
                .assertNoValues();
    }

    @Test
    public void whenGetLatestThenReturnNetwork() {
        TestObserver<String> testObserver = TestObserver.create();
        when(networkDataSource.getAndUpdate(parameter, cacheDataSource)).thenReturn(Maybe.just(NETWORK_DATA));

        sut.getLatest(parameter)
                .subscribe(testObserver);

        testObserver.assertResult(NETWORK_STRING); // includes .assertComplete().assertNoErrors()
        verify(networkDataSource).getAndUpdate(parameter, cacheDataSource);
        verifyZeroInteractions(cacheDataSource);
    }
}
