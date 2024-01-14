package com.cleanup.todoc.data;

import com.cleanup.todoc.BuildConfig;

public class BuildConfigResolver {

    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

}