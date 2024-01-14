package com.cleanup.todoc.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;
import java.util.Objects;

public class ProjectTasksRelation {

    @NonNull
    @Embedded
    private final ProjectEntity projectEntity;

    @NonNull
    @Relation(
            parentColumn = "idProject",
            entityColumn = "idProject"
    )
    private final List<TaskEntity> taskEntities;

    public ProjectTasksRelation(@NonNull ProjectEntity projectEntity, @NonNull List<TaskEntity> taskEntities) {
        this.projectEntity = projectEntity;
        this.taskEntities = taskEntities;
    }

    @NonNull
    public ProjectEntity getProjectEntity() {
        return projectEntity;
    }

    @NonNull
    public List<TaskEntity> getTaskEntities() {
        return taskEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectTasksRelation)) return false;
        ProjectTasksRelation that = (ProjectTasksRelation) o;
        return Objects.equals(projectEntity, that.projectEntity) && Objects.equals(taskEntities, that.taskEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectEntity, taskEntities);
    }

    @Override
    public String toString() {
        return "ProjectTasksRelation{" +
                "projectEntity=" + projectEntity +
                ", taskEntities=" + taskEntities +
                '}';
    }
}
