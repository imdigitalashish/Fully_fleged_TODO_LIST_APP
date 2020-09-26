package com.imdigitalashish.vacoder;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.room.Room;

import com.imdigitalashish.vacoder.database.Task;
import com.imdigitalashish.vacoder.database.TaskDatabase;

import java.util.List;

public class TodoListWidgetProvider extends AppWidgetProvider {


    private TaskDatabase db;
    private static final String TAG = "TodoListWidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId: appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


            Intent serviceIntent = new Intent(context, TodoListWidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.todo_list_widget);
            views.setOnClickPendingIntent(R.id.btn_widget, pendingIntent);
            views.setRemoteAdapter(R.id.lv_widget_items, serviceIntent);
            views.setEmptyView(R.id.lv_widget_items, R.id.example_widget_empty_view);

            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_widget_items);

        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.todo_list_widget);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Toast.makeText(context, "onDeleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context) {

        Toast.makeText(context, "onEnabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(Context context) {
        Toast.makeText(context, "onDisabled", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "App Widget Provider On Reciver Called");
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_widget_items);
        super.onReceive(context, intent);
    }
}
