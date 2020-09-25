package com.imdigitalashish.vacoder;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.imdigitalashish.vacoder.database.Task;
import com.imdigitalashish.vacoder.database.TaskDAO;
import com.imdigitalashish.vacoder.database.TaskDatabase;
import com.imdigitalashish.vacoder.database.TaskRepository;
import com.imdigitalashish.vacoder.database.TaskViewModel;

import java.util.List;

public class TodoListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetItemFactory(getApplicationContext(), intent);
    }


    class WidgetItemFactory implements RemoteViewsFactory {

        private Context context;
        private int appWidgetId;
        private String[] exampleData = {"one", "two", "three", "four", "five", "six", "seven", "nine", "ten"};
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

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return exampleData.length;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.todo_list_widget_item);
            views.setTextViewText(R.id.widget_item_text, exampleData[position]);
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
    }

}
