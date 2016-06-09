package com.example.check24.calculation;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;

/**
 * Created by marcingawel on 09.06.2016.
 */
public class CalculatorTest {
    @Test
    public void getSum() throws Exception {
        TestSubscriber<BigDecimal> sub = new TestSubscriber<>();
        List<Double> values = new ArrayList<>();
        values.add(Double.valueOf(1));
        values.add(Double.valueOf(-1));
        values.add(Double.valueOf(-1));
        Calculator calculator = new Calculator();

        calculator.getSum(values).subscribe(sub);
        sub.assertNoErrors();
        assertEquals(-1.0, sub.getOnNextEvents().get(0).doubleValue());
    }

}