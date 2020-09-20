package com.imdigitalashish.vacoder.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository taskRepository;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> pendingTasks;
    private LiveData<List<Task>> doneTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        allTasks = taskRepository.getAllTasks();
        pendingTasks = taskRepository.getPendingTasks();
        doneTasks = taskRepository.getDoneTasks();
    }

    public void insert(Task task) {
        taskRepository.insert(task);
    }

    public void update(Task task) {
        taskRepository.update(task);
    }

    public void delete(Task task) {
        taskRepository.delete(task);
    }

    public void deleteAllTask() {
        taskRepository.deleteAlltask();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<List<Task>> getPendingTasks() {
        return pendingTasks;
    }

    public LiveData<List<Task>> getDoneTasks() {
        return doneTasks;
    }
}
