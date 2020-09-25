package com.imdigitalashish.vacoder.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM task_table")
    void deleteAllTasks();

    @Query("SELECT * FROM task_table")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task_table WHERE done_or_note LIKE :condition ")
    LiveData<List<Task>> getTasksCond(boolean condition);

//    @Query("SELECT * FROM task_table WHERE done_or_note LIKE :condition")
//    List<Task> getTaskWidget(boolean condition);

}
