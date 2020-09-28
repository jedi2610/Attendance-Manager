package com.attendancemanager.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SundayDao {

    @Insert
    void insert(Sunday sunday);

    @Update
    void update(Sunday sunday);

    @Delete
    void delete(Sunday sunday);

    @Query("DELETE FROM sunday_table")
    void deleteAllSubjects();

    @Query("SELECT * FROM sunday_table")
    LiveData<List<SubjectMinimal>> getAllSubjects();
}