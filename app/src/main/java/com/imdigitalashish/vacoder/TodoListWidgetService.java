package com.imdigitalashish.vacoder;

import android.app.admin.DevicePolicyManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import com.imdigitalashish.vacoder.database.Task;
import com.imdigitalashish.vacoder.database.TaskDAO;
import com.imdigitalashish.vacoder.database.TaskDatabase;
import com.imdigitalashish.vacoder.database.TaskRepository;
import com.imdigitalashish.vacoder.database.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class TodoListWidgetService extends RemoteViewsService {

    private TaskDatabase db;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetItemFactory(getApplicationContext(), intent);
    }


    class WidgetItemFactory implements RemoteViewsFactory {

        private Context context;
        private int appWidgetId;
        private String[] exampleData = {"one", "two", "three", "four", "five", "six", "seven", "nine", "ten"};
        private ArrayList<String> data = new ArrayList<String>();
//        TaskViewModel taskViewModel;
//        TaskRepository taskRepository;
//        TaskDatabase taskDatabase;
//        private TaskDAO taskDAO;

        WidgetItemFactory(Context context, Intent intent) {
            this.context = context;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
//            data.clear();
            new DataAsyncTask().execute(context);
            SystemClock.sleep(300);

//            taskRepository = new TaskRepository(getApplication());
//            List<Task> tasks = taskRepository.getPendingTaskWidget();
//
////            taskDatabase = TaskDatabase.getInstance(getApplication());
////            taskDAO = taskDatabase.taskDAO();
////            List<Task> getPendingTasks = taskDAO.getTaskWidget(false);
////            Log.d("ASHISH_K", getPendingTasks.toString());

        }

        @Override
        public void onDataSetChanged() {

            // data.clear(); <-- We have to call this method to ensure that data is not loaded twice
            new DataAsyncTask().execute(context);
            SystemClock.sleep(300);
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {


            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.todo_list_widget_item);

//            views.setTextViewText(R.id.widget_item_text, exampleData[position]);
            views.setTextViewText(R.id.widget_item_text, data.get(position));
            Log.d("TAG", position+"");
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        private class DataAsyncTask extends AsyncTask<Context, Void, Void> {

            @Override
            protected Void doInBackground(Context... contexts) {

                db = Room.databaseBuilder(contexts[0],
                        TaskDatabase.class, "task_database").build();
                List<Task> tasks = db.taskDAO().getTaskWidget(false);
                Log.d("TAG_DONE", tasks.toString());
                for (Task task : tasks) {
                    if (!data.contains(task.getTitle())) {
                        data.add(task.getTitle());
                    }
                }
                Log.d("TAG_FOR", data.toString());
                return null;
            }
        }


    }



}
