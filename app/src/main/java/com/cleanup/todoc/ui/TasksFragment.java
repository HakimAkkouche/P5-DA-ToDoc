package com.cleanup.todoc.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cleanup.todoc.R;
import com.cleanup.todoc.ViewModelFactory;
import com.cleanup.todoc.databinding.FragmentTasksBinding;
import com.cleanup.todoc.ui.add_task.AddTaskDialogFragment;
import com.cleanup.todoc.ui.viewmodel.TasksViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TasksFragment extends Fragment implements NavigationListener {

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    private TasksViewModel mTasksViewModel;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        FragmentTasksBinding binding = FragmentTasksBinding.inflate(inflater, container, false);
        mTasksViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(TasksViewModel.class);

        TasksAdapter tasksAdapter = new TasksAdapter(taskId -> mTasksViewModel.onDeleteTaskButtonClicked(taskId));

        binding.listTasks.setAdapter(tasksAdapter);
        binding.fabAddTask.setOnClickListener(v -> displayAddTaskDialog());

        mTasksViewModel.getViewStateLiveData().observe(getViewLifecycleOwner(), tasksAdapter::submitList);


        return binding.getRoot();
    }

    /**
     * Initialize the contents of the Fragment host's standard options menu.  You
     * should place your menu items in to <var>menu</var>.  For this method
     * to be called, you must have first called {@link #setHasOptionsMenu}.  See
     * {@link MainActivity#onCreateOptionsMenu(Menu) Activity.onCreateOptionsMenu}
     * for more information.
     *
     * @param menu     The options menu in which you place your items.
     * @param inflater inflater
     * @see #setHasOptionsMenu
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.actions, menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.sort_alphabetical){
            mTasksViewModel.onAlphabeticalSortAscClicked();
        } else if (item.getItemId() == R.id.sort_alphabetical_inverted) {
            mTasksViewModel.onAlphabeticalSortDescClicked();
        } else if (item.getItemId() == R.id.sort_oldest_first) {
            mTasksViewModel.onChronologicalSortAscClicked();
        } else if (item.getItemId() == R.id.sort_recent_first) {
            mTasksViewModel.onChronologicalSortDescClicked();
        } else if (item.getItemId() == R.id.sort_by_project) {
            mTasksViewModel.onByProjectSortClicked();
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Shows the Dialog for adding a task
     */
    @Override
    public void displayAddTaskDialog() {
        AddTaskDialogFragment.newInstance().show(getParentFragmentManager(),null);
    }
}