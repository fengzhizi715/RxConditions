package com.safframework.rxcondition;

import io.reactivex.*;
import io.reactivex.internal.disposables.EmptyDisposable;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by tony on 2017/9/14.
 */
public class MaybeSwitchCase<T, K> extends Maybe<T> {

    final Callable<? extends K> caseSelector;

    final Map<? super K, ? extends MaybeSource<? extends T>> mapOfCases;

    final MaybeSource<? extends T> defaultCase;

    MaybeSwitchCase(Callable<? extends K> caseSelector,
                         Map<? super K, ? extends MaybeSource<? extends T>> mapOfCases,
                    MaybeSource<? extends T> defaultCase) {
        this.caseSelector = caseSelector;
        this.mapOfCases = mapOfCases;
        this.defaultCase = defaultCase;
    }

    @Override
    protected void subscribeActual(MaybeObserver<? super T> observer) {
        K key;
        MaybeSource<? extends T> source;

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
