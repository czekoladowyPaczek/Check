package com.example.check24.calculation;

import java.math.BigDecimal;
import java.util.List;

import rx.Observable;

/**
 * Created by marcingawel on 09.06.2016.
 */

public class Calculator {
    public Observable<BigDecimal> getSum(List<Double> numbers) {
        return Observable.defer(() -> Observable.create(f -> {
            BigDecimal sum = new BigDecimal(0);
            for (Double d : numbers) {
                if (!f.isUnsubscribed()) {
                    sum = sum.add(BigDecimal.valueOf(d.doubleValue()));
                } else {
                    break;
                }
            }
            if (!f.isUnsubscribed()) {
                f.onNext(sum);
                f.onCompleted();
            }
        }));
    }
}
