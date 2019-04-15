package com.guidovezzoni.architecture.repository;

import io.reactivex.Single;
import org.jetbrains.annotations.NotNull;

public abstract class BaseRepository<M, P> implements Repository<M, P> {

    @NotNull
    @Override
    public Single<M> get() {
        return get(null);
    }

    @NotNull
    @Override
    public Single<M> getLatest() {
        return getLatest(null);
    }
}
