package com.imdigitalashish.vacoder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.imdigitalashish.vacoder.database.Task;
import com.imdigitalashish.vacoder.database.TaskViewModel;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        setData();

        et_title_edit = findViewById(R.id.et_task_title_edit);
        radioGroup_edit = findViewById(R.id.rd_group_due_date_edit);
        datePicker_edit = findViewById(R.id.datePicker_edit);

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

        Task updateTask = new Task(title, dueDate_hai_ya_nahi, getIntent().getBooleanExtra("done_or_note", false), datePicker_edit.getDayOfMonth(), datePicker_edit.getMonth(), datePicker_edit.getYear());
        updateTask.setId(id);
        TaskViewModel taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.update(updateTask);
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
}