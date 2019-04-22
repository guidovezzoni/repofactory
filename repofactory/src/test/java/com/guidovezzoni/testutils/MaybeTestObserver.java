package com.guidovezzoni.testutils;

import io.reactivex.observers.TestObserver;

public class MaybeTestObserver<T> extends TestObserver<T> {

    public static <T> MaybeTestObserver<T> create() {
        return new MaybeTestObserver<T>();
    }

    @SuppressWarnings("UnusedReturnValue")
    public final TestObserver<T> assertCompletedEmpty() {
        return assertSubscribed()
                .assertComplete()
                .assertNoErrors()
                .assertNoValues();
    }

    @SuppressWarnings("UnusedReturnValue")
    public final TestObserver<T>  assertResult(T value) {
        return assertSubscribed()
                .assertValue(value)
                .assertNoErrors()
                .assertComplete();
    }
}
