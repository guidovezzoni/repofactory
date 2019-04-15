package com.guidovezzoni.architecture.datasource;

import com.guidovezzoni.architecture.cache.TimeStampHelper;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import kotlin.jvm.functions.Function1;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import retrofit2.Response;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RetrofitFunctionDataSourceTest {
    private static final Long TIMESTAMP = 47L;
    private static final String RESULT = "success!!!";
    private static final Response<String> SUCCESS = Response.success(RESULT);
    private static final double PARAM = 27.48;

    @Mock
    TimeStampHelper timeStampHelper;

    @Mock
    Function1<Double, Single<Response<String>>> endpointCall;

    private RetrofitFunctionDataSource<String, Double> sut;

    @Before
    public void setUp() throws Exception {
        when(endpointCall.invoke(PARAM)).thenReturn(Single.just(SUCCESS));

        sut = new RetrofitFunctionDataSource<>(timeStampHelper, endpointCall);
    }

    @Test
    public void whenGetFromEndPointThenFunctionCalled() {
        TestObserver<Response<String>> testObserver = TestObserver.create();

        sut.getFromEndPoint(PARAM)
                .subscribe(testObserver);

        //noinspection unchecked
        testObserver.assertResult(SUCCESS);

        verify(endpointCall).invoke(PARAM);
        verifyZeroInteractions(timeStampHelper);
    }

}
