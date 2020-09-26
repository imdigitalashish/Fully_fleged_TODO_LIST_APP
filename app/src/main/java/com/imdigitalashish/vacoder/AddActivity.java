package com.imdigitalashish.vacoder;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.KeyEventDispatcher;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RemoteViews;

import com.imdigitalashish.vacoder.database.Task;
import com.imdigitalashish.vacoder.database.TaskViewModel;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    EditText et_title;
    RadioGroup radioGroup;

    DatePicker datePicker;

    int valueOfNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        et_title = findViewById(R.id.et_task_title);
        radioGroup = findViewById(R.id.rd_group_due_date);
        datePicker = findViewById(R.id.datePicker);
        if (getIntent().hasExtra("NOTIFICATION_ID")) {
            int id = getIntent().getIntExtra("NOTIFICATION_ID", 1);

            valueOfNotification = id;
        } else {
            et_title.setText("NOTHING FOUND !");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_task_menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btn_menu_save:
                saveNote();
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void saveNote() {
        String title = et_title.getText().toString();
        boolean done_or_note = false;
        boolean dueDate;
        if (radioGroup.getCheckedRadioButtonId() == R.id.rd_btn_set_date) {
            dueDate = true;
        } else {
            dueDate = false;
        }

        int date = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();


        Task task = new Task(title, dueDate, done_or_note, date, month, year);
        TaskViewModel taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.insert(task);

//        Context context = this;
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgets, R.id.lv_widget_items);

        Intent intent = new Intent(this, TodoListWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), TodoListWidgetProvider.class));

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
//        intent.putExtra("UPDATE", "UPDATE");
        Log.d("AddActicity", "SEnding BroadCast");
        sendBroadcast(intent);

        finish();

    }

    public void setAlarm(Calendar c) {
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(AddActivity.class, )
    }

}