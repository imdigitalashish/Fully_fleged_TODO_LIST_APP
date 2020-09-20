package com.imdigitalashish.vacoder;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.imdigitalashish.vacoder.database.Task;
import com.imdigitalashish.vacoder.database.TaskViewModel;

public class AddActivity extends AppCompatActivity {

    EditText et_title;
    RadioGroup radioGroup;

    DatePicker datePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        et_title = findViewById(R.id.et_task_title);
        radioGroup = findViewById(R.id.rd_group_due_date);
        datePicker = findViewById(R.id.datePicker);
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

        Log.d("ASHISH", title + done_or_note + dueDate + date + month + year);

        Task task = new Task(title, dueDate, done_or_note, date, month, year);
        TaskViewModel taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.insert(task);
        Log.d("CODER", "Inserting Task Done");
    }

}