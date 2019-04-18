package com.guidovezzoni.architecture.repository;

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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class NoCacheRepositoryTest {
    private static final String NETWORK_STRING = "Network";
    private static final Long TIMESTAMP = 47L;
    private static final String REQUEST_FAILED = "Network request failed";

    private static final boolean USE_GET_LATEST = true;
    private static final boolean USE_GET = false;

    private static final boolean NETWORK_SUCCESS = true;
    private static final boolean NETWORK_FAILURE = false;

    @Mock
    private DataSource<String, Double> networkDataSource;

    private NoCacheRepository<String, Double> sut;

    @Parameterized.Parameters
    public static Iterable<? extends Object> data() {
        return Arrays.asList(27.48, 5.0, null, new Random().nextDouble());
    }

    @Parameterized.Parameter
    public Double parameter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sut = new NoCacheRepository<>(networkDataSource);
    }

    private void testGet(Double param, boolean getLatest, boolean succeeds) {
        if (succeeds) {
            when(networkDataSource.get(param)).thenReturn(Maybe.just(TimeStampedData.Companion.of(NETWORK_STRING, TIMESTAMP)));
        } else {
            when(networkDataSource.get(param)).thenReturn(Maybe.error(new Exception(REQUEST_FAILED)));
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
        testGet(parameter, USE_GET_LATEST, NETWORK_SUCCESS);
    }

    @Test
    public void whenGetLatestFailsThenReturnFromNetwork() {
        testGet(parameter, USE_GET_LATEST, NETWORK_FAILURE);
    }

    @Test
    public void whenGetSucceedsThenReturnFromNetwork() {
        testGet(parameter, USE_GET, NETWORK_SUCCESS);
    }

    @Test
    public void whenGetFailsThenReturnFromNetwork() {
        testGet(parameter, USE_GET, NETWORK_FAILURE);
    }
}
