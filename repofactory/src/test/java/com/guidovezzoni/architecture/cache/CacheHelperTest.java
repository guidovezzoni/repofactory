package com.guidovezzoni.architecture.cache;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class CacheHelperTest {

    private TimeStampHelper sut = new TimeStampHelper(() -> 15L);

    @Test
    public void whenIsTimeStampValidWithShorterValidityThenReturnFalse() {
        long currentTimeStamp = 5;
        long timeStamp = 1;
        long cacheValidity = 3;

        assertThat(TimeStampHelper.checkTimeStampValidity(currentTimeStamp, timeStamp, cacheValidity)).isFalse();
    }

    @Test
    public void whenIsTimeStampValidWithEqualValidityThenReturnTrue() {
        long currentTimeStamp = 5;
        long timeStamp = 1;
        long cacheValidity = 6;

        assertThat(TimeStampHelper.checkTimeStampValidity(currentTimeStamp, timeStamp, cacheValidity)).isTrue();
    }

    @Test
    public void whenIsTimeStampValidWithLongerValidityThenReturnTrue() {
        long currentTimeStamp = 5;
        long timeStamp = 1;
        long cacheValidity = 10;

        assertThat(TimeStampHelper.checkTimeStampValidity(currentTimeStamp, timeStamp, cacheValidity)).isTrue();
    }

    @Test
    public void whenIsCacheValidWithShorterValidityThenReturnFalse() {
        long cacheTimestamp = 5;
        long cacheValidity = 3;

        assertThat(sut.isCacheValid(cacheTimestamp, cacheValidity)).isFalse();
    }

    @Test
    public void whenIsCacheValidWithEqualValidityThenReturnTrue() {
        long cacheTimestamp = 5;
        long cacheValidity = 10;

        assertThat(sut.isCacheValid(cacheTimestamp, cacheValidity)).isTrue();
    }

    @Test
    public void whenIsCacheValidWithLongerValidityThenReturnTrue() {
        long cacheTimestamp = 5;
        long cacheValidity = 20;

        assertThat(sut.isCacheValid(cacheTimestamp, cacheValidity)).isTrue();
    }
}