package com.cleanup.todoc.ui.viewmodel;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cleanup.todoc.utils.Generated;

import java.util.Objects;

public abstract class TasksViewState {
    public enum Type {
        TASK,
        EMPTY_STATE
    }

    @NonNull
    protected final Type type;

    public TasksViewState(@NonNull Type type) {
        this.type = type;
    }

    @NonNull
    public Type getType() {
        return type;
    }

    @Override
    public abstract boolean equals(@Nullable Object obj);

    public static class Task extends TasksViewState {

        private final long mProjectId;
        @ColorInt
        private final int mProjectColor;
        private final String mNameProject;

        private final long mTaskId;
        @NonNull
        private final String mTaskName;
        private final long mCreationTimestamp;

        public Task( long projectId, String nameProject, @ColorInt int projectColor, long taskId, @NonNull String taskName, @NonNull long creationTimestamp) {
            super(Type.TASK);

            mProjectId = projectId;
            mNameProject = nameProject;
            mProjectColor = projectColor;
            mTaskId = taskId;
            mTaskName = taskName;
            mCreationTimestamp = creationTimestamp;
        }

        public String getNameProject() {
            return mNameProject;
        }

        @ColorInt
        public int getProjectColor() {
            return mProjectColor;
        }

        public long getTaskId() {
            return mTaskId;
        }
        @NonNull
        public String getTaskName() {
            return mTaskName;
        }

        public long getCreationTimestamp() {
            return mCreationTimestamp;
        }

        @Generated
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Task task = (Task) o;
            return mTaskId == task.mTaskId &&
                    mProjectColor == task.mProjectColor &&
                    mTaskName.equals(task.mTaskName);
        }

        @Generated
        @Override
        public int hashCode() {
            return Objects.hash(mTaskId, mProjectColor, mTaskName);
        }

        @Generated
        @NonNull
        @Override
        public String toString() {
            return "Task{" +
                    "type=" + type +
                    ", taskId=" + mTaskId +
                    ", projectColor=" + mProjectColor +
                    ", description='" + mTaskName + '\'' +
                    '}';
        }
    }

    public static class EmptyState extends TasksViewState {

        public EmptyState() {
            super(Type.EMPTY_STATE);
        }

        @Generated
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            return o != null && getClass() == o.getClass();
        }

        @Generated
        @NonNull
        @Override
        public String toString() {
            return "EmptyState{" +
                    "type=" + type +
                    '}';
        }
    }
}