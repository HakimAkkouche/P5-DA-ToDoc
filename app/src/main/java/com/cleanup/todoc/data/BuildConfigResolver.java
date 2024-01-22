package com.cleanup.todoc.data;

import com.cleanup.todoc.BuildConfig;

public class BuildConfigResolver {

    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    public boolean isRunningAndroidTest() {
        try {
            Class.forName("androidx.test.espresso.Espresso");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}