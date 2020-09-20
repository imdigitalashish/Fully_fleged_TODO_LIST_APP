package com.imdigitalashish.vacoder;

import android.icu.text.MessagePattern;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.imdigitalashish.vacoder.database.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Task> tasks = new ArrayList<>();
    public Task currentTask;

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_task, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        currentTask = tasks.get(position);
        holder.textViewTitle.setText(currentTask.getTitle());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks =tasks;
        notifyDataSetChanged();
    }

    class TaskHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle =itemView.findViewById(R.id.task_title);

        }
    }

}
