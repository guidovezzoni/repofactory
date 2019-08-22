package com.guidovezzoni.utils;

import org.junit.Before;
import org.junit.Test;
import retrofit2.Response;

public class RetrofitUtilsTest {
    private static final Response<String> RESPONSE_SUCCESS = Response.success("Correct!");
    private static final Response<String> RESPONSE_NULL = Response.success(null);
//    private static final Response<String> RESPONSE_ERROR = Response.error(401, );

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void unWrapResponse() {
    }
}