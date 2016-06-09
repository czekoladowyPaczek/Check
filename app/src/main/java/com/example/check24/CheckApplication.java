package com.example.check24;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.example.check24.graph.ActivityComponent;
import com.example.check24.graph.DaggerActivityComponent;

/**
 * Created by marcingawel on 09.06.2016.
 */

public class CheckApplication extends Application {

    private ActivityComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerActivityComponent.builder().build();
    }

    public ActivityComponent getComponent() {
        return component;
    }

    @VisibleForTesting
    public void setComponent(ActivityComponent component) {
        this.component = component;
    }
}
