package com.safframework.rxcondition;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.internal.disposables.EmptyDisposable;

/**
 * Created by tony on 2017/9/12.
 */
final class MaybeIfThen<T> extends Maybe<T>{

    final BooleanSupplier condition;

    final MaybeSource<? extends T> then;

    final MaybeSource<? extends T> orElse;

    MaybeIfThen(BooleanSupplier condition, MaybeSource<? extends T> then,
                MaybeSource<? extends T> orElse) {
        this.condition = condition;
        this.then = then;
        this.orElse = orElse;
    }

    @Override
    protected void subscribeActual(MaybeObserver<? super T> observer) {
        boolean b;

        try {
            b = condition.getAsBoolean();
        } catch (Throwable ex) {
            EmptyDisposable.error(ex, observer);
            return;
        }

        if (b) {
            then.subscribe(observer);
        } else {
            orElse.subscribe(observer);
        }
    }
}
