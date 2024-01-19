package com.cleanup.todoc.ui.add_task;


import android.app.Application;
import android.database.sqlite.SQLiteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.R;
import com.cleanup.todoc.data.BuildConfigResolver;
import com.cleanup.todoc.data.ToDocRepository;
import com.cleanup.todoc.data.entity.ProjectEntity;
import com.cleanup.todoc.data.entity.TaskEntity;
import com.cleanup.todoc.utils.EmptySingleLiveEvent;
import com.cleanup.todoc.utils.SingleLiveEvent;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class AddTaskViewModel extends ViewModel {

    @NonNull
    private final Application mApplication;
    @NonNull
    private final ToDocRepository mTaskRepository;
    @NonNull
    private final BuildConfigResolver mBuildConfigResolver;
    @NonNull
    private final Executor mMainExecutor;
    @NonNull
    private final Executor mExecutor;

    private final MediatorLiveData<AddTaskViewState> mAddTaskViewStateMediatorLiveData = new MediatorLiveData<>();

    private final MutableLiveData<Boolean> mIsAddingTaskInDatabaseMutableLiveData = new MutableLiveData<>();

    private final SingleLiveEvent<String> mDisplayToastMessageSingleLiveEvent = new SingleLiveEvent<>();

    private final EmptySingleLiveEvent mDismissDialogSingleLiveEvent = new EmptySingleLiveEvent();

    @Nullable
    private Long mProjectId;
    @Nullable
    private String mNameTask;

    public AddTaskViewModel(
            @NonNull Application application,
            @NonNull ToDocRepository toDocRepository,
            @NonNull BuildConfigResolver buildConfigResolver,
            @NonNull Executor mainExecutor,
            @NonNull Executor executor
    ) {
        this.mApplication = application;
        this.mTaskRepository = toDocRepository;
        this.mBuildConfigResolver = buildConfigResolver;
        this.mMainExecutor = mainExecutor;
        this.mExecutor = executor;

        LiveData<List<ProjectEntity>> allProjectsLiveData = toDocRepository.getAllProjectsLiveData();

        mAddTaskViewStateMediatorLiveData.addSource(allProjectsLiveData, projectEntities ->
                combine(projectEntities, mIsAddingTaskInDatabaseMutableLiveData.getValue())
        );

        mAddTaskViewStateMediatorLiveData.addSource(mIsAddingTaskInDatabaseMutableLiveData, isAddingTaskInDatabase ->
                combine(allProjectsLiveData.getValue(), isAddingTaskInDatabase)
        );
    }

    private void combine(@Nullable List<ProjectEntity> projectEntities, @Nullable Boolean isAddingTaskInDatabase) {
        if (projectEntities == null) {
            return;
        }

        List<AddTaskViewStateItem> addTaskViewStateItems = new ArrayList<>();

        for (ProjectEntity projectEntity : projectEntities) {
            addTaskViewStateItems.add(
                    new AddTaskViewStateItem(
                            projectEntity.getIdProject(),
                            projectEntity.getColorProject(),
                            projectEntity.getNameProject()
                    )
            );
        }

        mAddTaskViewStateMediatorLiveData.setValue(
                new AddTaskViewState(
                        addTaskViewStateItems,
                        isAddingTaskInDatabase != null && isAddingTaskInDatabase
                )
        );
    }

    public LiveData<AddTaskViewState> getAddTaskViewStateLiveData() {
        return mAddTaskViewStateMediatorLiveData;
    }

    public SingleLiveEvent<String> getDisplayToastMessageSingleLiveEvent() {
        return mDisplayToastMessageSingleLiveEvent;
    }

    public EmptySingleLiveEvent getDismissDialogSingleLiveEvent() {
        return mDismissDialogSingleLiveEvent;
    }

    public void onProjectSelected(long projectId) {
        this.mProjectId = projectId;
    }

    public void onTaskDescriptionChanged(String nameTask) {
        this.mNameTask = nameTask;
    }

    public void onOkButtonClicked() {
        if (mProjectId != null && mNameTask != null && !mNameTask.isEmpty()) {
            mIsAddingTaskInDatabaseMutableLiveData.setValue(true);

            mExecutor.execute(() -> {

                try {
                    mTaskRepository.addTask(new TaskEntity(mProjectId, mNameTask, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)));

                    mMainExecutor.execute(mDismissDialogSingleLiveEvent::call);
                } catch (SQLiteException e) {
                    if (mBuildConfigResolver.isDebug()) {
                        e.printStackTrace();
                    }

                    mMainExecutor.execute(() ->
                            mDisplayToastMessageSingleLiveEvent.setValue(mApplication.getString(R.string.error_add_task))
                    );
                }

                mMainExecutor.execute(() -> mIsAddingTaskInDatabaseMutableLiveData.setValue(false));
            });
        }
    }
}
