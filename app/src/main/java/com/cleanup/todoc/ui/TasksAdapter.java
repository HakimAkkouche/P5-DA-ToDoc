package com.cleanup.todoc.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.databinding.ItemTaskBinding;
import com.cleanup.todoc.databinding.ItemTaskEmptyBinding;
import com.cleanup.todoc.ui.viewmodel.TasksViewState;

import java.util.List;

/**
 * <p>Adapter which handles the list of tasks to display in the dedicated RecyclerView.</p>
 *
 * @author Hakim AKKOUCHE
 */
public class TasksAdapter extends ListAdapter<TasksViewState, RecyclerView.ViewHolder> {

    /**
     * The listener for when a task needs to be deleted
     */
    @NonNull
    private final TaskListener mTaskListener;

    /**
     * Instantiates a new TasksAdapter.
     *
     * @param taskListener the list of tasks the adapter deals with to set
     */
    public TasksAdapter(@NonNull TaskListener taskListener) {
        super(new TaskDiffCallback());
        this.mTaskListener = taskListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (TasksViewState.Type.values()[viewType]) {
            case TASK:
                return new TaskViewHolder(
                        ItemTaskBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false)
                );
            case EMPTY_STATE:
                return new RecyclerView.ViewHolder(
                        ItemTaskEmptyBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false).getRoot()
                ) {
                };
            default:
                throw new IllegalStateException("Unknown viewType : " + viewType);
        }
    }


    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link RecyclerView.ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link RecyclerView.ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(RecyclerView.ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TaskViewHolder) {
            ((TaskViewHolder) holder).bind((TasksViewState.Task) getItem(position), mTaskListener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType().ordinal();
    }

    /**
     * <p>ViewHolder for task items in the tasks list</p>
     *
     * @author Gaëtan HERFRAY
     */
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private final ItemTaskBinding mBinding;
        /**
         * Instantiates a new TaskViewHolder.
         *
         * @param binding the view of the task item
         */
        TaskViewHolder(@NonNull ItemTaskBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(@NonNull TasksViewState.Task item, @NonNull TaskListener listener) {
            mBinding.imgProject.setColorFilter(item.getProjectColor());
            mBinding.lblProjectName.setText(item.getNameProject());
            mBinding.lblTaskName.setText(item.getTaskName());
            mBinding.imgDelete.setOnClickListener(v -> listener.onDeleteTaskButtonClicked(item.getTaskId()));
        }
    }

    public static class TaskDiffCallback extends DiffUtil.ItemCallback<TasksViewState> {
        @Override
        public boolean areItemsTheSame(@NonNull TasksViewState oldItem, @NonNull TasksViewState newItem) {
            return (oldItem instanceof TasksViewState.Task && newItem instanceof TasksViewState.Task
                            && ((TasksViewState.Task) oldItem).getTaskId() == ((TasksViewState.Task) newItem).getTaskId()
            ) || (
                    oldItem instanceof TasksViewState.EmptyState && newItem instanceof TasksViewState.EmptyState
            );
        }

        @Override
        public boolean areContentsTheSame(@NonNull TasksViewState oldItem, @NonNull TasksViewState newItem) {
            return oldItem.equals(newItem);
        }
    }
}
