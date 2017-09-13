package com.safframework.rxcondition;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.MaybeSource;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.internal.disposables.EmptyDisposable;

/**
 * Created by tony on 2017/9/14.
 */
final class CompletableIfThen extends Completable {

    final BooleanSupplier condition;

    final CompletableSource then;

    final CompletableSource orElse;

    CompletableIfThen(BooleanSupplier condition, CompletableSource then,
                      CompletableSource orElse) {
        this.condition = condition;
        this.then = then;
        this.orElse = orElse;
    }

    @Override
    protected void subscribeActual(CompletableObserver observer) {
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
