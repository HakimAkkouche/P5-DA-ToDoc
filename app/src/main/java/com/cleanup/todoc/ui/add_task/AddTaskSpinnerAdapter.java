package com.cleanup.todoc.ui.add_task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;

import com.cleanup.todoc.R;
import com.cleanup.todoc.databinding.ProjectSpinnerItemBinding;

class AddTaskSpinnerAdapter extends ArrayAdapter<AddTaskViewStateItem> {
    public AddTaskSpinnerAdapter(@NonNull Context context) {
        super(context, R.layout.project_spinner_item);
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ProjectSpinnerItemBinding binding = ProjectSpinnerItemBinding.inflate(inflater, parent, false);

        AddTaskViewStateItem item = getItem(position);

        assert item != null;

        binding.projectColorSpinner.setColorFilter(item.getProjectColor());
        binding.projectNameSpinner.setText(item.getProjectName());
        binding.projectNameSpinner.setContentDescription(item.getProjectName());

        return binding.getRoot();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((AddTaskViewStateItem) resultValue).getProjectName();
            }
        };
    }
}
