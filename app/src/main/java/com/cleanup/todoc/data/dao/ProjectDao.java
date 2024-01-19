package com.cleanup.todoc.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.data.entity.ProjectEntity;

import java.util.List;

@Dao
public interface ProjectDao {
    @Query("select * from Project")
    List<ProjectEntity> getAllProjects();
    @Query("select * from Project")
    LiveData<List<ProjectEntity>> getAllLiveData();
    @Insert
    long insert(ProjectEntity projectEntity);
}
