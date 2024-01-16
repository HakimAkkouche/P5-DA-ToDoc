package com.cleanup.todoc.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.data.BuildConfigResolver;
import com.cleanup.todoc.data.ToDocRepository;
import com.cleanup.todoc.data.entity.ProjectTasksRelation;
import com.cleanup.todoc.data.entity.TaskEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executor;

public class TasksViewModel extends ViewModel {
    private final ToDocRepository mToDocRepository;
    private final Executor mExecutor;
    private final MediatorLiveData<List<TasksViewState>> mViewStateMediatorLiveData = new MediatorLiveData<>();
    private final MutableLiveData<TaskSortingType> mTaskSortingTypeMutableLiveData = new MutableLiveData<>();

    public TasksViewModel(ToDocRepository toDocRepository, Executor executor) {
        mToDocRepository = toDocRepository;
        mExecutor = executor;

        LiveData<List<ProjectTasksRelation>> projectsTasksLiveData = mToDocRepository.getAllTasksProjectLiveData();

        mViewStateMediatorLiveData.addSource(projectsTasksLiveData, projectsTasks ->
                combine(projectsTasks, mTaskSortingTypeMutableLiveData.getValue()));
        mViewStateMediatorLiveData.addSource(mTaskSortingTypeMutableLiveData, taskSortingType ->
                combine(projectsTasksLiveData.getValue(), taskSortingType)
        );

    }

    private void combine(@Nullable List<ProjectTasksRelation> projectsWithTasks, @Nullable TaskSortingType taskSortingType) {
        if (projectsWithTasks == null) {
            return;
        }

        List<TasksViewState> taskViewStates = new ArrayList<>();

        if (taskSortingType == TaskSortingType.ALPHABETICAL_ASC) {
            Set<TasksViewState.Task> set = new TreeSet<>((o1, o2) -> o1.getTaskName().compareTo(o2.getTaskName()));
            for (ProjectTasksRelation projectWithTasksEntity : projectsWithTasks) {
                for (TaskEntity taskEntity : projectWithTasksEntity.getTaskEntities()) {
                    set.add(mapItem(projectWithTasksEntity, taskEntity));
                }
            }
            taskViewStates.addAll(set);
        } else if (taskSortingType == TaskSortingType.ALPHABETICAL_DESC) {
            Set<TasksViewState.Task> set = new TreeSet<>((o1, o2) -> o2.getTaskName().compareTo(o1.getTaskName()));
            for (ProjectTasksRelation projectWithTasksEntity : projectsWithTasks) {
                for (TaskEntity taskEntity : projectWithTasksEntity.getTaskEntities()) {
                    set.add(mapItem(projectWithTasksEntity, taskEntity));
                }
            }
            taskViewStates.addAll(set);
        } else if (taskSortingType == TaskSortingType.CHRONOLOGICAL_ASC) {
            Set<TasksViewState.Task> set = new TreeSet<>((o1, o2) -> Long.compare(o1.getCreationTimestamp(), o2.getCreationTimestamp()));
            for (ProjectTasksRelation projectWithTasksEntity : projectsWithTasks) {
                for (TaskEntity taskEntity : projectWithTasksEntity.getTaskEntities()) {
                    set.add(mapItem(projectWithTasksEntity, taskEntity));
                }
            }
            taskViewStates.addAll(set);
        } else if (taskSortingType == TaskSortingType.CHRONOLOGICAL_DESC) {
            Set<TasksViewState.Task> set = new TreeSet<>((o1, o2) -> Long.compare(o2.getCreationTimestamp(), o1.getCreationTimestamp()));
            for (ProjectTasksRelation projectWithTasksEntity : projectsWithTasks) {
                for (TaskEntity taskEntity : projectWithTasksEntity.getTaskEntities()) {
                    set.add(mapItem(projectWithTasksEntity, taskEntity));
                }
            }
            taskViewStates.addAll(set);
        } else if (taskSortingType == TaskSortingType.PROJECT_SORT) {
            for (ProjectTasksRelation projectWithTasksEntity : projectsWithTasks) {
                for (TaskEntity taskEntity : projectWithTasksEntity.getTaskEntities()) {
                    taskViewStates.add(mapItem(projectWithTasksEntity, taskEntity));
                }
            }
        }else {
            Set<TasksViewState.Task> set = new TreeSet<>((o1, o2) -> Long.compare(o1.getTaskId(),o2.getTaskId()));
            for (ProjectTasksRelation projectWithTask : projectsWithTasks) {
                for (TaskEntity taskEntity : projectWithTask.getTaskEntities()) {
                    set.add(mapItem(projectWithTask, taskEntity));
                }
            }
            taskViewStates.addAll(set);
        }

        if (taskViewStates.isEmpty()) {
            taskViewStates.add(new TasksViewState.EmptyState());
        }

        mViewStateMediatorLiveData.setValue(taskViewStates);
    }

    private TasksViewState.Task mapItem(ProjectTasksRelation projectWithTask, TaskEntity taskEntity) {
        return new TasksViewState.Task(
                projectWithTask.getProjectEntity().getNameProject(),
                projectWithTask.getProjectEntity().getColorProject(),
                taskEntity.getIdTask(),
                taskEntity.getTaskName(),
                taskEntity.getCreationTimestamp()
        );
    }
    public void onAlphabeticalSortAscClicked(){
        mTaskSortingTypeMutableLiveData.setValue(TaskSortingType.ALPHABETICAL_ASC);
    }
    public void onAlphabeticalSortDescClicked(){
        mTaskSortingTypeMutableLiveData.setValue(TaskSortingType.ALPHABETICAL_DESC);
    }
    public void onChronologicalSortAscClicked(){
        mTaskSortingTypeMutableLiveData.setValue(TaskSortingType.CHRONOLOGICAL_ASC);

    }
    public void onChronologicalSortDescClicked(){
        mTaskSortingTypeMutableLiveData.setValue(TaskSortingType.CHRONOLOGICAL_DESC);
    }
    public void onByProjectSortClicked(){
        mTaskSortingTypeMutableLiveData.setValue(TaskSortingType.PROJECT_SORT);
    }

    public void onDeleteTaskButtonClicked(long taskId) {
        mExecutor.execute(() -> mToDocRepository.deleteTask(taskId));
    }


    @NonNull
    public LiveData<List<TasksViewState>> getViewStateLiveData() {
        return mViewStateMediatorLiveData;
    }
}
