package com.example.camperkids.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;

import java.util.List;

import com.example.camperkids.data.entities.Camp;

@Dao
public interface CampDao {
    @Query("SELECT * FROM camps")
    List<Camp> getAllCamps();

    @Query("SELECT * FROM camps WHERE id = :id LIMIT 1")
    Camp getCampById(int id);

    @Query("SELECT * FROM camps WHERE region_id = :regionId")
    List<Camp> getCampsForRegion(int regionId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCamp(Camp camp);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCamps(List<Camp> camps);

    @Update
    void updateCamp(Camp camp);

    @Delete
    void deleteCamp(Camp camp);
}