package com.guidovezzoni.architecture.cacheddatasource;

import com.guidovezzoni.architecture.cache.TimeStampHelper;
import com.guidovezzoni.model.TimeStampedData;
import com.guidovezzoni.testutils.MaybeTestObserver;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class MemoryCacheDataSourceTest {
    private static final TimeStampedData<String> CACHE_DATA_1 =
            TimeStampedData.Companion.of("Cache data string", TimeUnit.SECONDS.toMillis(1));

    private static final Double PARAM_2 = 3426345654.54;
    private static final TimeStampedData<String> CACHE_DATA_2 =
            TimeStampedData.Companion.of("Another cache string", TimeUnit.SECONDS.toMillis(2));

    private static final Double PARAM_3 = 6546324.765;
    private static final TimeStampedData<String> CACHE_DATA_3 =
            TimeStampedData.Companion.of("Last one", TimeUnit.SECONDS.toMillis(3));

    private static final Double PARAM_4 = 78673455.876876;
    private static final TimeStampedData<String> CACHE_DATA_4 =
            TimeStampedData.Companion.of("Actually one more", TimeUnit.SECONDS.toMillis(4));

    private static final Long CACHE_VALIDITY = TimeUnit.MINUTES.toMillis(5);
    private static final Long CACHE_VALIDITY_2 = TimeUnit.MINUTES.toMillis(15);

    @Mock
    TimeStampHelper timeStampHelper;

    @Mock
    CachedDataSource<String, Double> cachedDataSource;

    private MemoryCacheDataSource<String, Double> sut;

    @Parameterized.Parameters
    public static Iterable<Object> data() {
        return Arrays.asList(27.48, 5.0, null, new Random().nextDouble());
    }

    @Parameterized.Parameter
    public Double parameter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sut = new MemoryCacheDataSource<>(timeStampHelper);
    }

    @Test
    public void whenGetWithEmptyCacheThenReturnEmpty() {
        MaybeTestObserver<TimeStampedData<String>> testObserver = MaybeTestObserver.create();

        sut.get(parameter)
                .subscribe(testObserver);

        testObserver.assertCompletedEmpty();
    }

    @Test
    public void whenGetWithExpiredCacheThenReturnEmpty() {
        MaybeTestObserver<TimeStampedData<String>> testObserver = MaybeTestObserver.create();
        when(timeStampHelper.isCacheValid(CACHE_DATA_1, CACHE_VALIDITY)).thenReturn(true);
        sut.set(parameter, CACHE_DATA_1);
        when(timeStampHelper.isCacheValid(CACHE_DATA_1, CACHE_VALIDITY)).thenReturn(false);

        sut.get(parameter)
                .subscribe(testObserver);

        testObserver.assertCompletedEmpty();
        verify(timeStampHelper, times(2)).isCacheValid(CACHE_DATA_1, CACHE_VALIDITY);
    }

    @Test
    public void whenGetWithValidCacheThenReturnValue() {
        MaybeTestObserver<TimeStampedData<String>> testObserver = MaybeTestObserver.create();
        when(timeStampHelper.isCacheValid(CACHE_DATA_1, CACHE_VALIDITY)).thenReturn(true);
        sut.set(parameter, CACHE_DATA_1);

        sut.get(parameter)
                .subscribe(testObserver);

        testObserver.assertResult(CACHE_DATA_1);
        verify(timeStampHelper, times(2)).isCacheValid(CACHE_DATA_1, CACHE_VALIDITY);
    }

    @Test
    public void whenGetWithEmptyCacheAndMultipleCacheEntriesThenReturnEmpty() {
        MaybeTestObserver<TimeStampedData<String>> testObserver = MaybeTestObserver.create();
        when(timeStampHelper.isCacheValid(any(), anyLong())).thenReturn(true);
        sut.set(PARAM_2, CACHE_DATA_2);
        sut.set(PARAM_3, CACHE_DATA_3);
        sut.set(PARAM_4, CACHE_DATA_4);

        sut.get(parameter)
                .subscribe(testObserver);

        testObserver.assertCompletedEmpty();
    }

    @Test
    public void whenGetWithExpiredCacheAndMultipleCacheEntriesThenReturnValue() {
        MaybeTestObserver<TimeStampedData<String>> testObserver = MaybeTestObserver.create();
        when(timeStampHelper.isCacheValid(any(), anyLong())).thenReturn(true);
        sut.set(parameter, CACHE_DATA_1);
        sut.set(PARAM_2, CACHE_DATA_2);
        sut.set(PARAM_3, CACHE_DATA_3);
        sut.set(PARAM_4, CACHE_DATA_4);
        when(timeStampHelper.isCacheValid(CACHE_DATA_1, CACHE_VALIDITY)).thenReturn(false);

        sut.get(parameter)
                .subscribe(testObserver);

        testObserver.assertCompletedEmpty();
        verify(timeStampHelper, times(2)).isCacheValid(CACHE_DATA_1, CACHE_VALIDITY);
    }

    @Test
    public void whenGetWithValidCacheAndMultipleCacheEntriesThenReturnValue() {
        MaybeTestObserver<TimeStampedData<String>> testObserver = MaybeTestObserver.create();
        when(timeStampHelper.isCacheValid(any(), anyLong())).thenReturn(true);
        sut.set(parameter, CACHE_DATA_1);
        sut.set(PARAM_2, CACHE_DATA_2);
        sut.set(PARAM_3, CACHE_DATA_3);
        sut.set(PARAM_4, CACHE_DATA_4);

        sut.get(parameter)
                .subscribe(testObserver);

        testObserver.assertResult(CACHE_DATA_1);
        verify(timeStampHelper, times(2)).isCacheValid(CACHE_DATA_1, CACHE_VALIDITY);
    }

    @Test
    public void whenGetAndUpdateWithEmptyCacheThenReturnEmpty() {
        MaybeTestObserver<TimeStampedData<String>> testObserver = MaybeTestObserver.create();

        sut.getAndUpdate(parameter, cachedDataSource)
                .subscribe(testObserver);

        testObserver.assertCompletedEmpty();
        verifyZeroInteractions(cachedDataSource);
    }

    @Test
    public void whenGetAndUpdateWithExpiredCacheThenReturnEmpty() {
        MaybeTestObserver<TimeStampedData<String>> testObserver = MaybeTestObserver.create();
        when(timeStampHelper.isCacheValid(CACHE_DATA_1, CACHE_VALIDITY)).thenReturn(true);
        sut.set(parameter, CACHE_DATA_1);
        when(timeStampHelper.isCacheValid(CACHE_DATA_1, CACHE_VALIDITY)).thenReturn(false);

        sut.getAndUpdate(parameter, cachedDataSource)
                .subscribe(testObserver);

        testObserver.assertCompletedEmpty();
        verify(timeStampHelper, times(2)).isCacheValid(CACHE_DATA_1, CACHE_VALIDITY);
        verifyZeroInteractions(cachedDataSource);
    }

    @Test
    public void whenGetAndUpdateWithValidCacheThenReturnValue() {
        MaybeTestObserver<TimeStampedData<String>> testObserver = MaybeTestObserver.create();
        when(timeStampHelper.isCacheValid(CACHE_DATA_1, CACHE_VALIDITY)).thenReturn(true);
        sut.set(parameter, CACHE_DATA_1);

        sut.getAndUpdate(parameter, cachedDataSource)
                .subscribe(testObserver);

        testObserver.assertResult(CACHE_DATA_1);
        verify(timeStampHelper, times(2)).isCacheValid(CACHE_DATA_1, CACHE_VALIDITY);
        verify(cachedDataSource).set(parameter, CACHE_DATA_1);
    }

    @Test
    public void whenSetWithCacheEmptyThenReturnNewValue() {
        MaybeTestObserver<TimeStampedData<String>> testObserver = MaybeTestObserver.create();
        when(timeStampHelper.isCacheValid(CACHE_DATA_1, CACHE_VALIDITY)).thenReturn(true);

        sut.set(parameter, CACHE_DATA_1);

        sut.get(parameter)
                .subscribe(testObserver);
        testObserver.assertResult(CACHE_DATA_1);
        verify(timeStampHelper, times(2)).isCacheValid(CACHE_DATA_1, CACHE_VALIDITY);
    }

    @Test
    public void whenSetWithExistingCacheThenReturnLatest() {
        MaybeTestObserver<TimeStampedData<String>> testObserver = MaybeTestObserver.create();
        // set a value
        when(timeStampHelper.isCacheValid(CACHE_DATA_1, CACHE_VALIDITY)).thenReturn(true);
        sut.set(parameter, CACHE_DATA_1);
        // set another value
        when(timeStampHelper.isCacheValid(CACHE_DATA_2, CACHE_VALIDITY)).thenReturn(true);
        sut.set(parameter, CACHE_DATA_2);

        sut.get(parameter)
                .subscribe(testObserver);

        testObserver.assertResult(CACHE_DATA_2);
        verify(timeStampHelper).isCacheValid(CACHE_DATA_1, CACHE_VALIDITY);
        verify(timeStampHelper, times(2)).isCacheValid(CACHE_DATA_2, CACHE_VALIDITY);
    }

    @Test
    public void whenSetCacheValidityThenUpdateValue() {
        TestObserver<TimeStampedData<String>> testObserver = TestObserver.create();
        sut.setCacheValidity(CACHE_VALIDITY_2);
        when(timeStampHelper.isCacheValid(CACHE_DATA_1, CACHE_VALIDITY_2)).thenReturn(true);
        sut.set(parameter, CACHE_DATA_1);

        sut.get(parameter)
                .subscribe(testObserver);

        verify(timeStampHelper, times(2)).isCacheValid(CACHE_DATA_1, CACHE_VALIDITY_2);
    }


    @Test
    public void whenInvalidateCacheThenReturnEmpty() {
        MaybeTestObserver<TimeStampedData<String>> testObserver = MaybeTestObserver.create();
        when(timeStampHelper.isCacheValid(CACHE_DATA_1, CACHE_VALIDITY)).thenReturn(true);
        sut.set(parameter, CACHE_DATA_1);
        sut.invalidateCache();

        sut.get(parameter)
                .subscribe(testObserver);

        testObserver.assertCompletedEmpty();
        verify(timeStampHelper).isCacheValid(CACHE_DATA_1, CACHE_VALIDITY);
    }
}
