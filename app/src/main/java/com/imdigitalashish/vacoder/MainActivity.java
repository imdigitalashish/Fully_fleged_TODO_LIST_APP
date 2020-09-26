package com.imdigitalashish.vacoder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.imdigitalashish.vacoder.database.TaskViewModel;
import com.imdigitalashish.vacoder.fragments.allTasksFragment;
import com.imdigitalashish.vacoder.fragments.doneTasksFragment;
import com.imdigitalashish.vacoder.fragments.pendingTaskFragment;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, new pendingTaskFragment()).commit();

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.penginTasks);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.penginTasks) {
                    Toast.makeText(MainActivity.this, "Pending Task", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, new pendingTaskFragment()).commit();
                } else if (menuItem.getItemId() == R.id.doneTasks) {
                    Toast.makeText(MainActivity.this, "Done Task", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, new doneTasksFragment()).commit();
                } else if (menuItem.getItemId() == R.id.allTasks) {
                    Toast.makeText(MainActivity.this, "All Tasks", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, new allTasksFragment()).commit();
                }
                return true;
            }
        });
        
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                Toast.makeText(MainActivity.this, "You Are already on this tab", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addTask(View view) {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, new pendingTaskFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.penginTasks);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_all_menu_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAllTaksMenuActivity:
                TaskViewModel taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
                taskViewModel.deleteAllTask();
                Intent intent = new Intent(this, TodoListWidgetProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int[] ids = AppWidgetManager.getInstance(getApplication())
                        .getAppWidgetIds(new ComponentName(getApplication(), TodoListWidgetProvider.class));

                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
//        intent.putExtra("UPDATE", "UPDATE");
                Log.d("AddActicity", "SEnding BroadCast");
                sendBroadcast(intent);
                Toast.makeText(this, "All Task Deleted", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}