package com.imdigitalashish.vacoder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.imdigitalashish.vacoder.database.Task;
import com.imdigitalashish.vacoder.database.TaskViewModel;

import java.util.Calendar;

public class EditTask extends AppCompatActivity {

    private static final String TAG = "EditTask";

    String title;
    boolean dueDate;
    int date;
    int month;
    int year;

    EditText et_title_edit;
    RadioGroup radioGroup_edit;
    DatePicker datePicker_edit;
    TimePicker timePicker_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        setData();

        et_title_edit = findViewById(R.id.et_task_title_edit);
        radioGroup_edit = findViewById(R.id.rd_group_due_date_edit);
        datePicker_edit = findViewById(R.id.datePicker_edit);
        timePicker_edit = findViewById(R.id.timePickerAdddActivityEdit);
        timePicker_edit.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker_edit.setHour(getIntent().getIntExtra("hour", 1));
            timePicker_edit.setMinute(getIntent().getIntExtra("minute", 1));
        } else {
            timePicker_edit.setCurrentHour(getIntent().getIntExtra("hour", 1));
            timePicker_edit.setCurrentMinute(getIntent().getIntExtra("minute", 1));
        }

        et_title_edit.setText(title);
        RadioButton radioGroup = findViewById(R.id.rd_btn_set_date_edit);
        RadioButton radioGroup1 = findViewById(R.id.rd_btn_set_note_date_edit);
        if (dueDate) {
            radioGroup.setChecked(true);
        } else {
            radioGroup1.setChecked(true);
        }

        datePicker_edit.init(year, month, date, null);


    }

    private void setData() {
        title = getIntent().getStringExtra("title");
        dueDate = getIntent().getBooleanExtra("dueDate", false);
        date = getIntent().getIntExtra("date", 1);
        month = getIntent().getIntExtra("month", 1);
        year = getIntent().getIntExtra("year", 1);
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
                updateData();
                Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateData() {
        int id = getIntent().getIntExtra("id", 1);
        String title = et_title_edit.getText().toString();
        boolean dueDate_hai_ya_nahi;
        if (radioGroup_edit.getCheckedRadioButtonId() == R.id.rd_btn_set_date_edit) {
            dueDate_hai_ya_nahi = true;

        } else {
            dueDate_hai_ya_nahi = false;
        }

        int hour;
        int minute;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hour = timePicker_edit.getHour();
            minute = timePicker_edit.getMinute();
        } else {
            hour = timePicker_edit.getCurrentHour();
            minute = timePicker_edit.getCurrentMinute();
        }

        cancelAlarm(id);

        Task updateTask = new Task(title, dueDate_hai_ya_nahi, getIntent().getBooleanExtra("done_or_note", false), datePicker_edit.getDayOfMonth(), datePicker_edit.getMonth(), datePicker_edit.getYear(), hour, minute);
        updateTask.setId(id);
        TaskViewModel taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.update(updateTask);
        

        if (dueDate_hai_ya_nahi) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, datePicker_edit.getYear());
            c.set(Calendar.MONTH, datePicker_edit.getMonth());
            c.set(Calendar.DAY_OF_MONTH, datePicker_edit.getDayOfMonth());
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);

            updateAlarm(id, c);
        }

        
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

    private void updateAlarm(int id, Calendar c) {

        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(EditTask.this, MyNotificationService.class);
        intent.putExtra("nameview", et_title_edit.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getService(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        Log.d("ALARM", "Alarm Updated for Id : " + id);

    }

    private void cancelAlarm(int id) {

        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent cancelIntent = new Intent(EditTask.this, MyNotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(EditTask.this, id, cancelIntent, 0);
        manager.cancel(pendingIntent);
        Log.d("ALARM", "ALARM CANCELLED FOR  " + id);

    }
}