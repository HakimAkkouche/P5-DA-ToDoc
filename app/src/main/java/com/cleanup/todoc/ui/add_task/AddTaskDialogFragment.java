package com.cleanup.todoc.ui.add_task;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cleanup.todoc.ViewModelFactory;
import com.cleanup.todoc.databinding.DialogAddTaskBinding;

public class AddTaskDialogFragment extends DialogFragment {

    public static AddTaskDialogFragment newInstance() {
        return new AddTaskDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DialogAddTaskBinding binding = DialogAddTaskBinding.inflate(LayoutInflater.from(requireContext()));
        AddTaskViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(AddTaskViewModel.class);

        final AddTaskSpinnerAdapter adapter = new AddTaskSpinnerAdapter(requireContext());
        binding.projectAutocomplete.setAdapter(adapter);
        binding.projectAutocomplete.setOnItemClickListener((parent, view, position, id) ->
                viewModel.onProjectSelected(adapter.getItem(position).getProjectId())
        );
        binding.txtTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.onTaskDescriptionChanged(s.toString());
            }
        });
        binding.createTaskButtonCancel.setOnClickListener(v -> dismiss());
        binding.createTaskButtonOk.setOnClickListener(v -> viewModel.onOkButtonClicked());

        viewModel.getAddTaskViewStateLiveData().observe(this, addTaskViewState -> {
            adapter.clear();
            adapter.addAll(addTaskViewState.getAddTaskViewStateItems());

            binding.createTaskButtonOk.setVisibility(addTaskViewState.isProgressBarVisible() ? View.INVISIBLE : View.VISIBLE);
            binding.createTaskProgressBarOk.setVisibility(addTaskViewState.isProgressBarVisible() ? View.VISIBLE : View.INVISIBLE);
        });

        viewModel.getDismissDialogSingleLiveEvent().observe(this, ignored ->
                dismiss()
        );
        viewModel.getDisplayToastMessageSingleLiveEvent().observe(this, message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        );

        return binding.getRoot();
    }

}