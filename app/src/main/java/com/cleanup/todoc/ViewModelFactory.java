package com.cleanup.todoc;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.data.BuildConfigResolver;
import com.cleanup.todoc.data.ToDocDatabase;
import com.cleanup.todoc.data.ToDocRepository;
import com.cleanup.todoc.ui.add_task.AddTaskViewModel;
import com.cleanup.todoc.ui.viewmodel.TasksViewModel;
import com.cleanup.todoc.utils.MainThreadExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * The ViewModelFactory is the class responsible for the creation of every ViewModel in the application.
 * In this essence, its instance should be unique and available everywhere. This concept has a name :
 * this is the Singleton pattern (check the getInstance() method for more details).
 * Since the ViewModelFactory is the "entry point" for injection, this class will also be responsible
 * of injecting correctly the dependencies, creating a "graph" or "tree" of injection
 * In the end, the schema we could make about the injection is this :
 *       View       -->       ViewModel       -->     Repository     --> Datasource
 *                                ↑
 *                      Injection starts here,
 *                      in the ViewModel layer
 *
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private static volatile ViewModelFactory mFactory;
    private final BuildConfigResolver mBuildConfigResolver = new BuildConfigResolver();
    private final ToDocRepository mToDocRepository;
    private final Executor mExecutor = Executors.newFixedThreadPool(4);
    private final Executor mMainThreadExecutor = new MainThreadExecutor();

    public static ViewModelFactory getInstance() {
        if (mFactory == null) {
            synchronized (ViewModelFactory.class) {
                if (mFactory == null) {
                    mFactory = new ViewModelFactory();
                }
            }
        }
        return mFactory;
    }

    private ViewModelFactory() {
        Application mainApplication = MainApplication.getApplication();
        ToDocDatabase toDocDatabase = ToDocDatabase.getInstance(mainApplication, mExecutor, mBuildConfigResolver);

        mToDocRepository = new ToDocRepository(toDocDatabase.getProjectDao(), toDocDatabase.getTaskDao(),mBuildConfigResolver);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TasksViewModel.class)) {
            return (T) new TasksViewModel(mToDocRepository, mExecutor);
        } else if (modelClass.isAssignableFrom(AddTaskViewModel.class)) {
            return (T) new AddTaskViewModel(
                    MainApplication.getApplication(),
                    mToDocRepository,
                    mBuildConfigResolver,
                    mMainThreadExecutor,
                    mExecutor
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}