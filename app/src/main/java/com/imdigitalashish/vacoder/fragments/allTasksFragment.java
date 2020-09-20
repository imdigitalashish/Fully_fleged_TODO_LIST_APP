package com.imdigitalashish.vacoder.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imdigitalashish.vacoder.R;
import com.imdigitalashish.vacoder.TaskAdapter;
import com.imdigitalashish.vacoder.database.Task;
import com.imdigitalashish.vacoder.database.TaskViewModel;

import java.util.List;

public class allTasksFragment extends Fragment {

    private static final String TAG = "allTasksFragment";
    private TaskViewModel taskViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        RecyclerView recyclerView = getView().findViewById(R.id.alltasksfragmentView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final TaskAdapter adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        taskViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(getActivity(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.setTasks(tasks);
            }
        });

        return inflater.inflate(R.layout.fragment_all_tasks, container, false);

    }
}