package com.guidovezzoni.architecture.repository;

import com.guidovezzoni.architecture.cacheddatasource.MemoryCacheDataSource;
import com.guidovezzoni.architecture.datasource.DataSource;
import com.guidovezzoni.model.TimeStampedData;
import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SingleLevelCacheRepositoryTest {
    private static final String NETWORK_STRING = "Network";
    private static final String CACHE_STRING = "Cache";
    private static final Long TIMESTAMP = 47L;

    @Mock
    private DataSource<String, Void> networkDataSource;
    @Mock
    private MemoryCacheDataSource<String, Void> cacheDataSource;

    private SingleLevelCacheRepository<String, Void> sut;

    @Before
    public void setUp() {
        sut = new SingleLevelCacheRepository<>(networkDataSource, cacheDataSource);
    }

    @Test
    public void whenGetWithCacheAvailableThenReturnCache() {
        TestObserver<String> testObserver = TestObserver.create();
        when(cacheDataSource.get(null)).thenReturn(Maybe.just(TimeStampedData.Companion.of(CACHE_STRING, TIMESTAMP)));
        when(networkDataSource.getAndUpdate(null, cacheDataSource)).thenReturn(Maybe.just(TimeStampedData.Companion.of(NETWORK_STRING, TIMESTAMP)));

        sut.get(null).subscribe(testObserver);

        testObserver.assertResult(CACHE_STRING);  // includes .assertComplete().assertNoErrors()
        verify(cacheDataSource).get(null);
        verify(networkDataSource).getAndUpdate(null, cacheDataSource);
    }

    @Test
    public void whenGetWithCacheNotAvailableThenReturnFromNetwork() {
        TestObserver<String> testObserver = TestObserver.create();
        when(cacheDataSource.get(null)).thenReturn(Maybe.empty());
        when(networkDataSource.getAndUpdate(null, cacheDataSource)).thenReturn(Maybe.just(TimeStampedData.Companion.of(NETWORK_STRING, TIMESTAMP)));

        sut.get(null)
                .subscribe(testObserver);

        testObserver.assertResult(NETWORK_STRING); // includes .assertComplete().assertNoErrors()
        verify(cacheDataSource).get(null);
        verify(cacheDataSource, never()).set(any());
        verify(networkDataSource).getAndUpdate(null, cacheDataSource);
    }

    @Test
    public void whenGetWithNetworkFailureThenReturnHandleIt() {
        TestObserver<String> testObserver = TestObserver.create();
        when(cacheDataSource.get(null)).thenReturn(Maybe.empty());
        when(networkDataSource.getAndUpdate(null, cacheDataSource)).thenReturn(Maybe.error(new Exception("generic network error")));

        sut.get(null)
                .subscribe(testObserver);

        testObserver.assertNotComplete()
                .assertError(throwable -> throwable instanceof Exception)
                .assertNoValues();
    }

    @Test
    public void whenGetLatestThenReturnNetwork() {
        TestObserver<String> testObserver = TestObserver.create();
        when(networkDataSource.get(null)).thenReturn(Maybe.just(TimeStampedData.Companion.of(NETWORK_STRING, TIMESTAMP)));

        sut.getLatest(null)
                .subscribe(testObserver);

        testObserver.assertResult(NETWORK_STRING); // includes .assertComplete().assertNoErrors()

        verify(networkDataSource).get(null);
        verify(cacheDataSource, never()).get(null);
        verify(cacheDataSource).set(any());
    }
}
