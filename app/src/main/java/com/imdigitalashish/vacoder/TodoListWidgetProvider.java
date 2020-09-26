package com.imdigitalashish.vacoder;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.imdigitalashish.vacoder.database.TaskDatabase;

public class TodoListWidgetProvider extends AppWidgetProvider {


    private TaskDatabase db;
    private static final String TAG = "TodoListWidgetProvider";
    public static final String ACTION_TOAST = "actionToast";

    public static final String EXTRA_ITEM_TEXT = "extraItemPosition";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId: appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


            Intent serviceIntent = new Intent(context, TodoListWidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));


            Intent clickIntent = new Intent(context, TodoListWidgetProvider.class);
            clickIntent.setAction(ACTION_TOAST);
            PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context,
                    0, clickIntent, 0);


            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.todo_list_widget);
            views.setOnClickPendingIntent(R.id.btn_widget, pendingIntent);
            views.setRemoteAdapter(R.id.lv_widget_items, serviceIntent);
            views.setEmptyView(R.id.lv_widget_items, R.id.example_widget_empty_view);
            // TODO: SET PENDING INTENT TEMPLATE TOO
            views.setPendingIntentTemplate(R.id.lv_widget_items, clickPendingIntent);

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
        if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_widget_items);
        } else if (ACTION_TOAST.equals(intent.getAction())){
            String clickedPosition = intent.getStringExtra(EXTRA_ITEM_TEXT);
            Toast.makeText(context, clickedPosition, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Clicked Called");
        }

        super.onReceive(context, intent);
    }
}
