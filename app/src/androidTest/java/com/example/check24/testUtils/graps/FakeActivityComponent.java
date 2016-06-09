package com.example.check24.testUtils.graps;

import com.example.check24.graph.ActivityComponent;
import com.example.check24.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by marcingawel on 09.06.2016.
 */
@Singleton
@Component(modules = FakeCalculatorModule.class)
public interface FakeActivityComponent extends ActivityComponent {
    void inject(MainActivity activity);
}
