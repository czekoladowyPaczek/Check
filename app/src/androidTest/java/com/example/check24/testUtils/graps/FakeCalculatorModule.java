package com.example.check24.testUtils.graps;

import com.example.check24.calculation.Calculator;

import dagger.Module;
import dagger.Provides;

/**
 * Created by marcingawel on 09.06.2016.
 */
@Module
public class FakeCalculatorModule {

    private Calculator calc;

    public FakeCalculatorModule(Calculator calc) {
        this.calc = calc;
    }

    @Provides
    Calculator calculator() {
        return calc;
    }
}
