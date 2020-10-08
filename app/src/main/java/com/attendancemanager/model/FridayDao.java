package com.attendancemanager.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FridayDao {

    @Insert
    void insert(Friday friday);

    @Update
    void update(Friday friday);

    @Delete
    void delete(Friday friday);

    @Query("DELETE FROM friday_table")
    void deleteAllSubjects();

    @Query("UPDATE friday_table SET status = -1")
    void resetStatus();

    @Query("DELETE FROM friday_table where subjectName = :subjectName")
    void deleteSubject(String subjectName);

    @Query("SELECT * FROM friday_table")
    LiveData<List<SubjectMinimal>> getAllSubjects();

    @Query("SELECT * FROM friday_table ORDER BY id")
    List<SubjectMinimal> getSubjectList();
}
