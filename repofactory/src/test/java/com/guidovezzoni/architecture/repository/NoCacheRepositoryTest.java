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

    @Mock
    private DataSource<String, Void> networkDataSource;

    private NoCacheRepository<String, Void> sut;

    @Before
    public void setUp() {
        sut = new NoCacheRepository<>(networkDataSource);
    }

    @Test
    public void whenGetThenReturnFromNetwork() {
        TestObserver<String> testObserver = TestObserver.create();
        when(networkDataSource.get(null)).thenReturn(Maybe.just(TimeStampedData.Companion.of(NETWORK_STRING)));

        sut.get(null)
                .subscribe(testObserver);

        testObserver.assertResult(NETWORK_STRING);   // includes .assertComplete().assertNoErrors()
        verify(networkDataSource).get(null);
    }

    @Test
    public void whenGetLatestThenReturnFromNetwork() {
        TestObserver<String> testObserver = TestObserver.create();
        when(networkDataSource.get(null)).thenReturn(Maybe.just(TimeStampedData.Companion.of(NETWORK_STRING)));

        sut.getLatest(null)
                .subscribe(testObserver);

        testObserver.assertResult(NETWORK_STRING);   // includes .assertComplete().assertNoErrors()
        verify(networkDataSource).get(null);
    }
}
