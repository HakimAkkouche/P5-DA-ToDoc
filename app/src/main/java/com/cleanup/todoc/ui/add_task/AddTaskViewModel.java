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
    private final Application application;
    @NonNull
    private final ToDocRepository taskRepository;
    @NonNull
    private final BuildConfigResolver buildConfigResolver;
    @NonNull
    private final Executor mainExecutor;
    @NonNull
    private final Executor ioExecutor;

    private final MediatorLiveData<AddTaskViewState> addTaskViewStateMediatorLiveData = new MediatorLiveData<>();

    private final MutableLiveData<Boolean> isAddingTaskInDatabaseMutableLiveData = new MutableLiveData<>();

    private final SingleLiveEvent<String> displayToastMessageSingleLiveEvent = new SingleLiveEvent<>();

    private final EmptySingleLiveEvent dismissDialogSingleLiveEvent = new EmptySingleLiveEvent();

    @Nullable
    private Long projectId;
    @Nullable
    private String taskDescription;

    public AddTaskViewModel(
            @NonNull Application application,
            @NonNull ToDocRepository taskRepository,
            @NonNull BuildConfigResolver buildConfigResolver,
            @NonNull Executor mainExecutor,
            @NonNull Executor ioExecutor
    ) {
        this.application = application;
        this.taskRepository = taskRepository;
        this.buildConfigResolver = buildConfigResolver;
        this.mainExecutor = mainExecutor;
        this.ioExecutor = ioExecutor;

        LiveData<List<ProjectEntity>> allProjectsLiveData = taskRepository.getAllProjectsLiveData();

        addTaskViewStateMediatorLiveData.addSource(allProjectsLiveData, projectEntities ->
                combine(projectEntities, isAddingTaskInDatabaseMutableLiveData.getValue())
        );

        addTaskViewStateMediatorLiveData.addSource(isAddingTaskInDatabaseMutableLiveData, isAddingTaskInDatabase ->
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

        addTaskViewStateMediatorLiveData.setValue(
                new AddTaskViewState(
                        addTaskViewStateItems,
                        isAddingTaskInDatabase != null && isAddingTaskInDatabase
                )
        );
    }

    public LiveData<AddTaskViewState> getAddTaskViewStateLiveData() {
        return addTaskViewStateMediatorLiveData;
    }

    public SingleLiveEvent<String> getDisplayToastMessageSingleLiveEvent() {
        return displayToastMessageSingleLiveEvent;
    }

    public EmptySingleLiveEvent getDismissDialogSingleLiveEvent() {
        return dismissDialogSingleLiveEvent;
    }

    public void onProjectSelected(long projectId) {
        this.projectId = projectId;
    }

    public void onTaskDescriptionChanged(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void onOkButtonClicked() {
        if (projectId != null && taskDescription != null && !taskDescription.isEmpty()) {
            isAddingTaskInDatabaseMutableLiveData.setValue(true);

            ioExecutor.execute(() -> {

                try {
                    taskRepository.addTask(new TaskEntity(projectId, taskDescription, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)));

                    mainExecutor.execute(() -> dismissDialogSingleLiveEvent.call());
                } catch (SQLiteException e) {
                    if (buildConfigResolver.isDebug()) {
                        e.printStackTrace();
                    }

                    mainExecutor.execute(() ->
                            displayToastMessageSingleLiveEvent.setValue(application.getString(R.string.error_add_task))
                    );
                }

                mainExecutor.execute(() -> isAddingTaskInDatabaseMutableLiveData.setValue(false));
            });
        }
    }
}
