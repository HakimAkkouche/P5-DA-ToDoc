package com.cleanup.todoc.data.entity;

import android.speech.SpeechRecognizer;

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
        parentColumns = "idProject",
        childColumns = "idProject"
        )
)
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    private final long idTask;

    @ColumnInfo(index = true)
    private final long idProject;

    @NonNull
    private final String taskName;

    @NonNull
    private final long creationTimestamp;

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
        this.idTask = idTask;
        this.idProject = idProject;
        this.taskName = taskName;
        this.creationTimestamp = creationTimestamp;
    }

    public long getIdTask() {
        return idTask;
    }

    public long getIdProject() {
        return idProject;
    }

    @NonNull
    public String getTaskName() {
        return taskName;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskEntity)) return false;
        TaskEntity that = (TaskEntity) o;
        return idTask == that.idTask
                && idProject == that.idProject
                && Objects.equals(taskName, that.taskName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTask, idProject, taskName, creationTimestamp);
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
                "idTask=" + idTask +
                ", idProject=" + idProject +
                ", taskName='" + taskName + '\'' +
                ", creationTimestamp=" + creationTimestamp +
                '}';
    }
}
