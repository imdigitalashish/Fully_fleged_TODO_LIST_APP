package com.imdigitalashish.vacoder;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.room.Room;

import com.imdigitalashish.vacoder.database.Task;
import com.imdigitalashish.vacoder.database.TaskDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.imdigitalashish.vacoder.TodoListWidgetProvider.EXTRA_ITEM_DATE;
import static com.imdigitalashish.vacoder.TodoListWidgetProvider.EXTRA_ITEM_DUEDATE;
import static com.imdigitalashish.vacoder.TodoListWidgetProvider.EXTRA_ITEM_HOUR;
import static com.imdigitalashish.vacoder.TodoListWidgetProvider.EXTRA_ITEM_ID;
import static com.imdigitalashish.vacoder.TodoListWidgetProvider.EXTRA_ITEM_MINUTE;
import static com.imdigitalashish.vacoder.TodoListWidgetProvider.EXTRA_ITEM_MONTH;
import static com.imdigitalashish.vacoder.TodoListWidgetProvider.EXTRA_ITEM_TEXT;
import static com.imdigitalashish.vacoder.TodoListWidgetProvider.EXTRA_ITEM_YEAR;

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
        List<Task> tasks;

        WidgetItemFactory(Context context, Intent intent) {
            this.context = context;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            new DataAsyncTask().execute(context);
            SystemClock.sleep(300);
        }

        @Override
        public void onDataSetChanged() {
            data.clear();
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
            Intent fillIntent = new Intent();

            fillIntent.putExtra(EXTRA_ITEM_ID, tasks.get(position).getId());
            fillIntent.putExtra(EXTRA_ITEM_TEXT, tasks.get(position).getTitle());
            fillIntent.putExtra(EXTRA_ITEM_DUEDATE, tasks.get(position).isDueDate());
            fillIntent.putExtra(EXTRA_ITEM_DATE, tasks.get(position).getDate());
            fillIntent.putExtra(EXTRA_ITEM_MONTH, tasks.get(position).getMonth());
            fillIntent.putExtra(EXTRA_ITEM_YEAR, tasks.get(position).getYear());
            fillIntent.putExtra(EXTRA_ITEM_HOUR, tasks.get(position).getHour());
            fillIntent.putExtra(EXTRA_ITEM_MINUTE, tasks.get(position).getMinute());


            views.setOnClickFillInIntent(R.id.widget_item_relativeLayout, fillIntent);

            views.setTextViewText(R.id.widget_item_text, data.get(position));
            views.setTextViewText(R.id.item_number_widget, String.valueOf(position+1));
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
                tasks = db.taskDAO().getTaskWidget(false);
                Log.d("TAG_DONE", tasks.toString());
                for (Task task : tasks) {
                        data.add(task.getTitle());
                }
                Log.d("TAG_FOR", data.toString());
                return null;
            }
        }


    }



}
