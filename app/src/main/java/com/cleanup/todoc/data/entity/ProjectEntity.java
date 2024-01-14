package com.cleanup.todoc.data.entity;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "Project")
public class ProjectEntity {
    @PrimaryKey(autoGenerate = true)
    private final long idProject;

    @NonNull
    private final String nameProject;

    @ColorInt
    private final int colorProject;
    @Ignore
    public ProjectEntity(@NonNull String projectName, @ColorInt int colorInt) {
        this(0, projectName, colorInt);
    }
    @VisibleForTesting
    public ProjectEntity(long idProject, @NonNull String nameProject, int colorProject) {
        this.idProject = idProject;
        this.nameProject = nameProject;
        this.colorProject = colorProject;
    }

    public long getIdProject() {
        return idProject;
    }

    @NonNull
    public String getNameProject() {
        return nameProject;
    }

    public int getColorProject() {
        return colorProject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectEntity)) return false;
        ProjectEntity that = (ProjectEntity) o;
        return idProject == that.idProject
                && colorProject == that.colorProject
                && Objects.equals(nameProject, that.nameProject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProject, nameProject, colorProject);
    }

    @Override
    public String toString() {
        return "ProjectEntity{" +
                "idProject=" + idProject +
                ", nameProject='" + nameProject + '\'' +
                ", colorProject=" + colorProject +
                '}';
    }
}
