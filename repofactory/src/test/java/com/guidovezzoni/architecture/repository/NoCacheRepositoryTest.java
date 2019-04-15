package com.guidovezzoni.architecture.repository;

import com.guidovezzoni.architecture.datasource.DataSource;
import com.guidovezzoni.model.TimeStampedData;
import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NoCacheRepositoryTest {
    private static final String NETWORK_STRING = "Network";
    private static final Long TIMESTAMP = 47L;
    private static final Double PARAM = 27.48;
    private static final Double PARAM_NULL = null;
    private static final String REQUEST_FAILED = "Network request failed";

    private static final boolean USE_GET_LATEST = true;
    private static final boolean USE_GET = false;

    private static final boolean NETWORK_SUCCESS = true;
    private static final boolean NETWORK_FAILURE = false;

    @Mock
    private DataSource<String, Double> networkDataSource;

    private NoCacheRepository<String, Double> sut;

    @Before
    public void setUp() {
        sut = new NoCacheRepository<>(networkDataSource);
    }

    private void testGet(Double param, boolean getLatest, boolean succeeds) {
        if (succeeds) {
            when(networkDataSource.get(PARAM)).thenReturn(Maybe.just(TimeStampedData.Companion.of(NETWORK_STRING, TIMESTAMP)));
            when(networkDataSource.get(null)).thenReturn(Maybe.just(TimeStampedData.Companion.of(NETWORK_STRING, TIMESTAMP)));
        } else {
            when(networkDataSource.get(PARAM)).thenReturn(Maybe.error(new Exception(REQUEST_FAILED)));
            when(networkDataSource.get(null)).thenReturn(Maybe.error(new Exception(REQUEST_FAILED)));
        }

        TestObserver<String> testObserver = TestObserver.create();

        if (getLatest) {
            sut.getLatest(param)
                    .subscribe(testObserver);
        } else {
            sut.get(param)
                    .subscribe(testObserver);
        }

        if (succeeds) {
            testObserver.assertResult(NETWORK_STRING);
            verify(networkDataSource).get(param);
        } else {
            testObserver.assertFailureAndMessage(Exception.class, REQUEST_FAILED);
            verify(networkDataSource).get(param);
        }
    }


    @Test
    public void whenGetLatestSucceedsThenReturnFromNetwork() {
        testGet(PARAM, USE_GET_LATEST, NETWORK_SUCCESS);
    }

    @Test
    public void whenGetLatestNullSucceedsThenReturnFromNetwork() {
        testGet(PARAM_NULL, USE_GET_LATEST, NETWORK_SUCCESS);
    }

    @Test
    public void whenGetLatestFailsThenReturnFromNetwork() {
        testGet(PARAM, USE_GET_LATEST, NETWORK_FAILURE);
    }

    @Test
    public void whenGetLatestNullFailsThenReturnFromNetwork() {
        testGet(PARAM_NULL, USE_GET_LATEST, NETWORK_FAILURE);
    }


    @Test
    public void whenGetSucceedsThenReturnFromNetwork() {
        testGet(PARAM, USE_GET, NETWORK_SUCCESS);
    }

    @Test
    public void whenGetNullSucceedsThenReturnFromNetwork() {
        testGet(PARAM_NULL, USE_GET, NETWORK_SUCCESS);
    }

    @Test
    public void whenGetFailsThenReturnFromNetwork() {
        testGet(PARAM, USE_GET, NETWORK_FAILURE);
    }

    @Test
    public void whenGetNullFailsThenReturnFromNetwork() {
        testGet(PARAM_NULL, USE_GET, NETWORK_FAILURE);
    }
}
