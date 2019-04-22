package com.guidovezzoni.architecture.datasource;

import com.guidovezzoni.architecture.cache.TimeStampHelper;
import com.guidovezzoni.architecture.cacheddatasource.CachedDataSource;
import com.guidovezzoni.model.TimeStampedData;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import retrofit2.Response;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BaseRetrofitDataSourceTest {
    private static final String RESULT = "success!!!";
    private static final Long TIMESTAMP = 47L;
    private static final TimeStampedData<String> TIME_STAMPED_DATA = TimeStampedData.Companion.of(RESULT, TIMESTAMP);
    private static final double PARAMS = 27.45;

    @Mock
    TimeStampHelper timeStampHelper;

    @Mock
    CachedDataSource<String, Double> cachedDataSource;

    private BaseRetrofitDataSource<String, Double> sutSuccess;

    private BaseRetrofitDataSource<String, Double> sutFail;

    @Before
    public void setUp() {
        when(timeStampHelper.getCurrentTimeStamp()).thenReturn(TIMESTAMP);

        sutSuccess = new BaseRetrofitDataSource<String, Double>(timeStampHelper) {
            @Override
            protected Single<Response<String>> getFromEndPoint(Double params) {
                return Single.just(Response.success(RESULT));
            }
        };

        sutFail = new BaseRetrofitDataSource<String, Double>(timeStampHelper) {
            @Override
            protected Single<Response<String>> getFromEndPoint(Double params) {
                return Single.error(new Exception("Network request failed"));
            }
        };
    }

    @Test
    public void whenGetSucceedsThenReturnFromNetwork() {
        TestObserver<TimeStampedData<String>> testObserver = TestObserver.create();

        sutSuccess.get(PARAMS)
                .subscribe(testObserver);

        //noinspection unchecked
        testObserver.assertResult(TIME_STAMPED_DATA);
        verify(timeStampHelper).getCurrentTimeStamp();
    }

    @Test
    public void whenGetFailsThenReturnError() {
        TestObserver<TimeStampedData<String>> testObserver = TestObserver.create();

        sutFail.get(PARAMS)
                .subscribe(testObserver);

        //noinspection unchecked
        testObserver.assertFailureAndMessage(Exception.class, "Network request failed");
        verifyZeroInteractions(timeStampHelper);
    }

    @Test
    public void whenGetAndUpdateSucceedsThenReturnFromNetwork() {
        TestObserver<TimeStampedData<String>> testObserver = TestObserver.create();

        sutSuccess.getAndUpdate(PARAMS, cachedDataSource)
                .subscribe(testObserver);

        //noinspection unchecked
        testObserver.assertResult(TIME_STAMPED_DATA);
        verify(timeStampHelper).getCurrentTimeStamp();
        verify(cachedDataSource).set(PARAMS, TIME_STAMPED_DATA);
    }

    @Test
    public void whenGetAndUpdateFailsThenReturnError() {
        TestObserver<TimeStampedData<String>> testObserver = TestObserver.create();

        sutFail.getAndUpdate(PARAMS, cachedDataSource)
                .subscribe(testObserver);

        //noinspection unchecked
        testObserver.assertFailureAndMessage(Exception.class, "Network request failed");
        verifyZeroInteractions(timeStampHelper);
        verifyZeroInteractions(cachedDataSource);
    }
}