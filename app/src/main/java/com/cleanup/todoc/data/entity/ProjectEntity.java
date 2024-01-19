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
    private final long mIdProject;

    @NonNull
    private final String mNameProject;

    @ColorInt
    private final int mColorProject;
    @Ignore
    public ProjectEntity(@NonNull String projectName, @ColorInt int colorInt) {
        this(0, projectName, colorInt);
    }
    @VisibleForTesting
    public ProjectEntity(long idProject, @NonNull String nameProject, int colorProject) {
        this.mIdProject = idProject;
        this.mNameProject = nameProject;
        this.mColorProject = colorProject;
    }

    public long getIdProject() {
        return mIdProject;
    }

    @NonNull
    public String getNameProject() {
        return mNameProject;
    }

    public int getColorProject() {
        return mColorProject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectEntity)) return false;
        ProjectEntity that = (ProjectEntity) o;
        return mIdProject == that.mIdProject
                && mColorProject == that.mColorProject
                && Objects.equals(mNameProject, that.mNameProject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mIdProject, mNameProject, mColorProject);
    }

    @Override
    public String toString() {
        return "ProjectEntity{" +
                "idProject=" + mIdProject +
                ", nameProject='" + mNameProject + '\'' +
                ", colorProject=" + mColorProject +
                '}';
    }
}
