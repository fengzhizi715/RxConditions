package com.safframework.rxcondition;

import io.reactivex.*;
import io.reactivex.annotations.NonNull;
import io.reactivex.internal.disposables.EmptyDisposable;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by tony on 2017/9/14.
 */
public class SingleSwitchCase<T, K> extends Single<T> {

    final Callable<? extends K> caseSelector;

    final Map<? super K, ? extends SingleSource<? extends T>> mapOfCases;

    final SingleSource<? extends T> defaultCase;

    SingleSwitchCase(Callable<? extends K> caseSelector,
                         Map<? super K, ? extends SingleSource<? extends T>> mapOfCases,
                     SingleSource<? extends T> defaultCase) {
        this.caseSelector = caseSelector;
        this.mapOfCases = mapOfCases;
        this.defaultCase = defaultCase;
    }

    @Override
    protected void subscribeActual(@NonNull SingleObserver<? super T> observer) {
        K key;
        SingleSource<? extends T> source;

        try {
            key = caseSelector.call();

            source = mapOfCases.get(key);
        } catch (Throwable ex) {
            EmptyDisposable.error(ex, observer);
            return;
        }

        if (source == null) {
            source = defaultCase;
        }

        source.subscribe(observer);
    }
}
