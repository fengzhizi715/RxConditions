package com.safframework.rxcondition;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.subscriptions.EmptySubscription;
import org.reactivestreams.Publisher;

/**
 * Created by tony on 2017/9/14.
 */
final class SingleIfThen<T> extends Single<T> {

    final BooleanSupplier condition;

    final SingleSource<? extends T> then;

    final SingleSource<? extends T> orElse;

    SingleIfThen(BooleanSupplier condition, SingleSource<? extends T> then,
                 SingleSource<? extends T> orElse) {
        this.condition = condition;
        this.then = then;
        this.orElse = orElse;
    }

    @Override
    protected void subscribeActual(@NonNull SingleObserver<? super T> observer) {
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
