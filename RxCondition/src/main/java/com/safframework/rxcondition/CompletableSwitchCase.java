package com.safframework.rxcondition;

import io.reactivex.*;
import io.reactivex.internal.disposables.EmptyDisposable;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by tony on 2017/9/14.
 */
final class CompletableSwitchCase<K> extends Completable {

    final Callable<? extends K> caseSelector;

    final Map<? super K, ? extends CompletableSource> mapOfCases;

    final CompletableSource defaultCase;

    CompletableSwitchCase(Callable<? extends K> caseSelector,
                         Map<? super K, ? extends CompletableSource> mapOfCases,
                          CompletableSource defaultCase) {
        this.caseSelector = caseSelector;
        this.mapOfCases = mapOfCases;
        this.defaultCase = defaultCase;
    }

    @Override
    protected void subscribeActual(CompletableObserver observer) {
        K key;
        CompletableSource source;

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
