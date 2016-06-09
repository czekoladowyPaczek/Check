package com.example.check24.graph;

import com.example.check24.calculation.Calculator;

import dagger.Module;
import dagger.Provides;

/**
 * Created by marcingawel on 09.06.2016.
 */
@Module
public class CalculatorModule {

    @Provides
    public Calculator calculator() {
        return new Calculator();
    }

}
