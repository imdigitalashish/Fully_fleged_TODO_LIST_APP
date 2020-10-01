package com.imdigitalashish.vacoder.fragments;

import android.content.Intent;
import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.imdigitalashish.vacoder.EditTask;
import com.imdigitalashish.vacoder.R;
import com.imdigitalashish.vacoder.TaskAdapter;
import com.imdigitalashish.vacoder.database.Task;
import com.imdigitalashish.vacoder.database.TaskViewModel;

import java.util.List;

public class doneTasksFragment extends Fragment {

    TaskViewModel taskViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_done_tasks, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.donetaskfragmentView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final TaskAdapter adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        taskViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);
        taskViewModel.getDoneTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.setTasks(tasks);
            }
        });
        adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                Intent intent = new Intent(getActivity(), EditTask.class);
                intent.putExtra("id", task.getId());
                intent.putExtra("title", task.getTitle());
                intent.putExtra("dueDate", task.isDueDate());
                intent.putExtra("done_or_note", task.isDone_or_note());
                intent.putExtra("date", task.getDate());
                intent.putExtra("month", task.getMonth());
                intent.putExtra("year", task.getYear());
                intent.putExtra("hour", task.getHour());
                intent.putExtra("minute", task.getMinute());
                startActivity(intent);
            }
        });

        final Task[] deletedTask = new Task[1];

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        deletedTask[0] = adapter.getTaskAt(position);
                        Task task = adapter.getTaskAt(position);
                        taskViewModel.delete(task);
                        Snackbar.make(recyclerView, deletedTask[0].getTitle(), Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        taskViewModel.insert(deletedTask[0]);
                                        adapter.notifyDataSetChanged();
                                    }
                                }).show();
                        break;
                    case ItemTouchHelper.RIGHT:
                        break;
                }
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;

    }



}