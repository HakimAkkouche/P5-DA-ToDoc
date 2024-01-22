package com.cleanup.todoc.data;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import com.cleanup.todoc.data.dao.ProjectDao;
import com.cleanup.todoc.data.dao.TaskDao;
import com.cleanup.todoc.data.entity.ProjectEntity;
import com.cleanup.todoc.data.entity.ProjectTasksRelation;
import com.cleanup.todoc.data.entity.TaskEntity;

import java.util.List;

/**
 * A Repository is a source of data. In this project, we are using the LiveData to stream our data through
 * the layers to the View (the Activity). In this spirit, the Repository should always return a LiveData
 * and handle the Thread switching itself, since the LiveData is always working on the Main Thread.
 */
public class ToDocRepository {

    private final TaskDao mTaskDao;
    private final ProjectDao mProjectDao;

    public ToDocRepository(ProjectDao projectDao, TaskDao taskDao) {
        this.mTaskDao = taskDao;
        this.mProjectDao = projectDao;
    }

    @MainThread
    public LiveData<List<ProjectEntity>> getAllProjectsLiveData() {
        return mProjectDao.getAllLiveData();
    }
    @MainThread
    public LiveData<List<ProjectTasksRelation>> getAllTasksProjectLiveData() {
        return mTaskDao.getAllProjectTaskRelated();
    }
    @WorkerThread
    public void addTask(@NonNull TaskEntity taskEntity) {
        mTaskDao.insert(taskEntity);
    }
    @WorkerThread
    public void deleteTask(long idTask) {
        mTaskDao.delete(idTask);
    }
}
