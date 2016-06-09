package com.example.check24.graph;

import com.example.check24.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by marcingawel on 09.06.2016.
 */
@Singleton
@Component(modules = {CalculatorModule.class})
public interface ActivityComponent {
    void inject(MainActivity activity);
}
