package com.cleanup.todoc.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "Task",
        foreignKeys = @ForeignKey(
        entity = ProjectEntity.class,
        parentColumns = "mIdProject",
        childColumns = "mIdProject"
        )
)
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    private final long mIdTask;

    @ColumnInfo(index = true)
    private final long mIdProject;

    @NonNull
    private final String mTaskName;


    private final long mCreationTimestamp;

    @Ignore
    public TaskEntity(long projectId, @NonNull String taskDescription, long creationTimestamp) {
        this(0, projectId, taskDescription, creationTimestamp);
    }

    /**
     * Room constructor only
     * @param idTask primary key
     * @param idProject foreign key
     * @param taskName task string
     * @param creationTimestamp creation time
     */
    public TaskEntity(long idTask, long idProject, @NonNull String taskName, long creationTimestamp) {
        this.mIdTask = idTask;
        this.mIdProject = idProject;
        this.mTaskName = taskName;
        this.mCreationTimestamp = creationTimestamp;
    }

    public long getIdTask() {
        return mIdTask;
    }

    public long getIdProject() {
        return mIdProject;
    }

    @NonNull
    public String getTaskName() {
        return mTaskName;
    }

    public long getCreationTimestamp() {
        return mCreationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskEntity)) return false;
        TaskEntity that = (TaskEntity) o;
        return mIdTask == that.mIdTask
                && mIdProject == that.mIdProject
                && Objects.equals(mTaskName, that.mTaskName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mIdTask, mIdProject, mTaskName, mCreationTimestamp);
    }

    @NonNull
    @Override
    public String toString() {
        return "TaskEntity{" +
                "idTask=" + mIdTask +
                ", idProject=" + mIdProject +
                ", taskName='" + mTaskName + '\'' +
                ", creationTimestamp=" + mCreationTimestamp +
                '}';
    }
}
