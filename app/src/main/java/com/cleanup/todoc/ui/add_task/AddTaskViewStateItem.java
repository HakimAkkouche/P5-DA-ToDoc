package com.cleanup.todoc.ui.add_task;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.cleanup.todoc.utils.Generated;

import java.util.Objects;

public class AddTaskViewStateItem {

    private final long mProjectId;

    @ColorInt
    private final int mProjectColor;

    @NonNull
    private final String mProjectName;

    public AddTaskViewStateItem(long projectId, @ColorInt int projectColor, @NonNull String projectName) {
        this.mProjectId = projectId;
        this.mProjectColor = projectColor;
        this.mProjectName = projectName;
    }

    public long getProjectId() {
        return mProjectId;
    }

    @ColorInt
    public int getProjectColor() {
        return mProjectColor;
    }

    @NonNull
    public String getProjectName() {
        return mProjectName;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddTaskViewStateItem that = (AddTaskViewStateItem) o;
        return mProjectId == that.mProjectId &&
                mProjectColor == that.mProjectColor &&
                mProjectName.equals(that.mProjectName);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(mProjectId, mProjectColor, mProjectName);
    }

    @Generated
    @NonNull
    @Override
    public String toString() {
        return "AddTaskViewStateItem{" +
                "projectId=" + mProjectId +
                ", projectColor=" + mProjectColor +
                ", projectName='" + mProjectName + '\'' +
                '}';
    }
}
