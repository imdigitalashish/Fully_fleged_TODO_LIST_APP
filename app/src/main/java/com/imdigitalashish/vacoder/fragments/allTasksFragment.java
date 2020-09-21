package com.imdigitalashish.vacoder.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.imdigitalashish.vacoder.EditTask;
import com.imdigitalashish.vacoder.R;
import com.imdigitalashish.vacoder.TaskAdapter;
import com.imdigitalashish.vacoder.database.Task;
import com.imdigitalashish.vacoder.database.TaskViewModel;

import java.util.List;

public class allTasksFragment extends Fragment {

    TaskViewModel taskViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_tasks, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.alltasksfragmentView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final TaskAdapter adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        taskViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.setTasks(tasks);
            }
        });

//        adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Task task) {
//                Intent intent = new Intent(getActivity(), EditTask.class);
//                intent.putExtra("")
//            }
//        });
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
//
//            @Override
//            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//                ColorDrawable background = new ColorDrawable(Color.RED);
//                background.setBounds(recyclerView.getRight(), recyclerView.getTop(), (int) (recyclerView.getLeft() - dX), 100);
//                background.draw(c);
//            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        return view;
    }
}