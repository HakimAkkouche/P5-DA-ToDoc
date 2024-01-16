package com.cleanup.todoc.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.data.entity.ProjectTasksRelation;
import com.cleanup.todoc.data.entity.TaskEntity;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("select * from Project")
    LiveData<List<ProjectTasksRelation>> getAllProjectTaskRelated();
    @Insert
    long insert(TaskEntity taskEntity);
    @Query("delete from Task where idTask=:idTask")
    int delete(long idTask);
}
