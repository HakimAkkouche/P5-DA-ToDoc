package com.cleanup.todoc.ui.add_task;


import androidx.annotation.NonNull;

import com.cleanup.todoc.utils.Generated;

import java.util.List;
import java.util.Objects;

public class AddTaskViewState {

    @NonNull
    private final List<AddTaskViewStateItem> mAddTaskViewStateItems;

    private final boolean mIsProgressBarVisible;

    public AddTaskViewState(@NonNull List<AddTaskViewStateItem> addTaskViewStateItems, boolean isProgressBarVisible) {
        this.mAddTaskViewStateItems = addTaskViewStateItems;
        this.mIsProgressBarVisible = isProgressBarVisible;
    }

    @NonNull
    public List<AddTaskViewStateItem> getAddTaskViewStateItems() {
        return mAddTaskViewStateItems;
    }

    public boolean isProgressBarVisible() {
        return mIsProgressBarVisible;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddTaskViewState that = (AddTaskViewState) o;
        return mIsProgressBarVisible == that.mIsProgressBarVisible &&
                mAddTaskViewStateItems.equals(that.mAddTaskViewStateItems);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(mAddTaskViewStateItems, mIsProgressBarVisible);
    }

    @NonNull
    @Generated
    @Override
    public String toString() {
        return "AddTaskViewState{" +
                "addTaskViewStateItems=" + mAddTaskViewStateItems +
                ", isProgressBarVisible=" + mIsProgressBarVisible +
                '}';
    }
}
