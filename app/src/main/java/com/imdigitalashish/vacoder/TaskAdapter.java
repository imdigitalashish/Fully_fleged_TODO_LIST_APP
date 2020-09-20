package com.imdigitalashish.vacoder;

import android.content.Context;
import android.icu.text.MessagePattern;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.imdigitalashish.vacoder.database.Task;
import com.imdigitalashish.vacoder.database.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Task> tasks = new ArrayList<>();
    private OnItemClickListener listener;
    public Task currentTask;

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_task, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskHolder holder, int position) {
        currentTask = tasks.get(position);
        final Context context = holder.itemView.getContext();
        holder.textViewTitle.setText(currentTask.getTitle());
        holder.checkBox.setChecked(currentTask.isDone_or_note());
        
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show();
                    currentTask.setDone_or_note(true);
                } else {
                    Toast.makeText(context, "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (holder.checkBox.isChecked()) {
//                    Log.d("CODING_IN_FLOW", "HE:");
//                    currentTask.setDone_or_note(true);
//                    TaskViewModel taskViewModel = ViewModelProviders.of((FragmentActivity) context).get(TaskViewModel.class);
//                    taskViewModel.update(currentTask);
//                    Toast.makeText(context, "Done Updating the task`", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public Task getTaskAt(int position) {
        return tasks.get(position);
    }

    public void setTasks(List<Task> tasks) {
        this.tasks =tasks;
        notifyDataSetChanged();
    }

    class TaskHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private CheckBox checkBox;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle =itemView.findViewById(R.id.task_title);
            checkBox = itemView.findViewById(R.id.checkbox_done_or_not);


        }
    }

    public interface OnItemClickListener {
        void onItemClick(Task task);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
