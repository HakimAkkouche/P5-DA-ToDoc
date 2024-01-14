package com.cleanup.todoc.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.cleanup.todoc.data.entity.ProjectEntity;
import com.cleanup.todoc.databinding.ProjectSpinnerItemBinding;

import java.util.List;

public class ProjectAdapter extends ArrayAdapter<ProjectEntity> {
    /**
     * Constructor. This constructor will result in the underlying data collection being
     * immutable, so methods such as {@link #clear()} will throw an exception.
     *
     * @param context  The current context.
     * @param projectEntities  The objects to represent in the ListView.
     */
    public ProjectAdapter(@NonNull Context context, List<ProjectEntity> projectEntities) {
        super(context, android.R.layout.simple_spinner_dropdown_item, projectEntities);
    }


    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @NonNull
    public View getCustomView(int position, @NonNull ViewGroup parent) {

        ProjectSpinnerItemBinding binding = ProjectSpinnerItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        ProjectEntity projectEntity = getItem(position);

        binding.projectColorSpinner.setColorFilter(projectEntity.getColorProject());
        binding.projectNameSpinner.setText(projectEntity.getNameProject()+"test");

        return binding.getRoot();
    }
}
