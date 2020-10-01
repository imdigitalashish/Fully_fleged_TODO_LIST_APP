package com.imdigitalashish.vacoder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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

import static android.content.Context.ALARM_SERVICE;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Task> tasks = new ArrayList<>();
    public Task currentTask;
    private OnItemClickListener listener;
    TaskViewModel taskViewModel;

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_task, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskHolder holder, final int position) {
        currentTask = tasks.get(position);
        final Context context = holder.itemView.getContext();
        holder.textViewTitle.setText(currentTask.getTitle());
        holder.checkBox.setChecked(currentTask.isDone_or_note());
        
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    Task clicked = tasks.get(position);
                    Log.d("CODING", clicked.getTitle());
                    Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show();
                    clicked.setDone_or_note(true);

                    taskViewModel = ViewModelProviders.of((FragmentActivity) context).get(TaskViewModel.class);
                    taskViewModel.update(clicked);

                    AlarmManager manager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
                    Intent CancelIntent = new Intent(context, MyNotificationService.class);
                    PendingIntent pendingIntent = PendingIntent.getService(context, clicked.getId(), CancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    manager.cancel(pendingIntent);

                    Intent intent = new Intent(context, TodoListWidgetProvider.class);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    int[] ids = AppWidgetManager.getInstance(((FragmentActivity) context).getApplication())
                            .getAppWidgetIds(new ComponentName(((FragmentActivity) context).getApplication(), TodoListWidgetProvider.class));

                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                    Log.d("AddActicity", "SEnding BroadCast");
                    context.sendBroadcast(intent);
                    Toast.makeText(context, currentTask.getTitle(), Toast.LENGTH_SHORT).show();
                } else {
                    Task clicked = tasks.get(position);
                    clicked.setDone_or_note(false);
                    taskViewModel = ViewModelProviders.of((FragmentActivity)context).get(TaskViewModel.class);
                    taskViewModel.update(clicked);
                    Toast.makeText(context, "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
            textViewTitle = itemView.findViewById(R.id.task_title);
            checkBox = itemView.findViewById(R.id.checkbox_done_or_not);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(tasks.get(position));
                    }
                }
            });


        }
    }

    public interface OnItemClickListener {
        void onItemClick(Task task);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
