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
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.TimePicker;
import android.widget.Toast;

import com.imdigitalashish.vacoder.database.Task;
import com.imdigitalashish.vacoder.database.TaskViewModel;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

public class AddActivity extends AppCompatActivity {

    EditText et_title;
    RadioGroup radioGroup;

    DatePicker datePicker;
    TimePicker timePicker;

    int valueOfNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        et_title = findViewById(R.id.et_task_title);
        radioGroup = findViewById(R.id.rd_group_due_date);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePickerAdddActivity);
        timePicker.setIs24HourView(true);

        RadioButton radioButton = findViewById(R.id.rd_btn_set_note_date);
        radioButton.setChecked(true);

        if (getIntent().hasExtra("NOTIFICATION_ID")) {
            int id = getIntent().getIntExtra("NOTIFICATION_ID", 1);

            valueOfNotification = id;

            Log.d("ALARM", "AddActivity: GOT A NOTIFICATION OF  VALUE FOR : " + valueOfNotification);
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
        int hour;
        int minute;

        if (!title.trim().equals("")) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                hour = timePicker.getHour();
                minute = timePicker.getMinute();
            } else {
                hour = timePicker.getCurrentHour();
                minute = timePicker.getCurrentMinute();
            }


            Log.d("ALARM", dueDate+"");
            if (dueDate) {
                Calendar c = Calendar.getInstance();

                c.set(Calendar.YEAR, datePicker.getYear());
                c.set(Calendar.MONTH, datePicker.getMonth());
                c.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                c.set(Calendar.HOUR_OF_DAY, hour);
                c.set(Calendar.MINUTE, minute);
                c.set(Calendar.SECOND, 0);

                setAlarm(c);
            }



            Task task = new Task(title, dueDate, done_or_note, date, month, year, hour, minute);
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
        } else {
            Toast.makeText(this, "Please Fill All the details", Toast.LENGTH_SHORT).show();
        }


    }

    private void setAlarm(Calendar c) {

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(AddActivity.this, MyNotificationService.class);
        intent.putExtra("nameview", et_title.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getService(this, valueOfNotification, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        Log.d("ALARM", "Alarm Setted for With ID : " + valueOfNotification);

    }

}