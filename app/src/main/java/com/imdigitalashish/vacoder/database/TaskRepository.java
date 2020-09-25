package com.imdigitalashish.vacoder.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {

    private TaskDAO taskDAO;

    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> pendingTasks;
    private LiveData<List<Task>> doneTasks;

    public TaskRepository(Application application) {
        TaskDatabase taskDatabase = TaskDatabase.getInstance(application);
        taskDAO = taskDatabase.taskDAO();
        allTasks = taskDAO.getAllTasks();
        pendingTasks = taskDAO.getTasksCond(false);
        doneTasks = taskDAO.getTasksCond(true);
    }

    public void insert(Task task) {
        new InsertAysncTask(taskDAO).execute(task);
    }

    public void update(Task task) {
        new UpdateAsyncTask(taskDAO).execute(task);
    }

    public void delete(Task task) {
        new DeleteAsyncTask(taskDAO).execute(task);
    }

    public void deleteAlltask() {
        new DeleteAllAsyncTask(taskDAO).execute();
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


    public static class InsertAysncTask extends AsyncTask<Task, Void, Void> {

        private TaskDAO taskDAO;

        private InsertAysncTask(TaskDAO taskDAO) {
            this.taskDAO = taskDAO;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDAO.insert(tasks[0]);
            return null;
        }
    }

    public static class UpdateAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDAO taskDAO;
        public UpdateAsyncTask(TaskDAO taskDAO) {
            this.taskDAO = taskDAO;

        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDAO.update(tasks[0]);
            return null;
        }
    }

    public static class DeleteAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDAO taskDAO;
        public DeleteAsyncTask(TaskDAO taskDAO) {
            this.taskDAO = taskDAO;

        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDAO.delete(tasks[0]);
            return null;
        }
    }

    public static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private TaskDAO taskDAO;
        public DeleteAllAsyncTask(TaskDAO taskDAO) {
            this.taskDAO = taskDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDAO.deleteAllTasks();
            return null;
        }
    }

}
