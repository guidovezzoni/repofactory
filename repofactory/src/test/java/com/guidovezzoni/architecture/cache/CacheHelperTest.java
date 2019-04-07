package com.guidovezzoni.architecture.cache;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class CacheHelperTest {

    private CacheHelper sut = new CacheHelper(System::currentTimeMillis);

    @Test
    public void whenIsTimeStampValidWithShorterValidityThenReturnFalse() {
        long currentTimeStamp = 5;
        long timeStamp = 1;
        long cacheValidity = 3;

        assertThat(sut.isTimeStampValid(currentTimeStamp, timeStamp, cacheValidity)).isFalse();
    }

    @Test
    public void whenIsTimeStampValidWithEqualValidityThenReturnTrue() {
        long currentTimeStamp = 5;
        long timeStamp = 1;
        long cacheValidity = 6;

        assertThat(sut.isTimeStampValid(currentTimeStamp, timeStamp, cacheValidity)).isTrue();
    }

    @Test
    public void whenIsTimeStampValidWithLongerValidityThenReturnTrue() {
        long currentTimeStamp = 5;
        long timeStamp = 1;
        long cacheValidity = 10;

        assertThat(sut.isTimeStampValid(currentTimeStamp, timeStamp, cacheValidity)).isTrue();
    }
}