package com.example.camperkids.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;

import java.util.List;

import com.example.camperkids.data.entities.Period;

@Dao
public interface PeriodDao {
    @Query("SELECT * FROM periods")
    List<Period> getAllPeriods();

    @Query("SELECT * FROM periods WHERE id = :id LIMIT 1")
    Period getPeriodById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPeriod(Period period);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPeriods(List<Period> periods);

    @Update
    void updatePeriod(Period period);

    @Delete
    void deletePeriod(Period period);
}