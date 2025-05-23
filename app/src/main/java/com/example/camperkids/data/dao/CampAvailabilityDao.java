package com.example.camperkids.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;

import java.util.List;

import com.example.camperkids.data.entities.CampAvailability;

@Dao
public interface CampAvailabilityDao {
    @Query("SELECT * FROM camp_availability")
    List<CampAvailability> getAllAvailabilities();

    @Query("SELECT * FROM camp_availability WHERE id = :id LIMIT 1")
    CampAvailability getAvailabilityById(int id);

    @Query("SELECT * FROM camp_availability WHERE camp_id = :campId")
    List<CampAvailability> getAvailabilitiesForCamp(int campId);

    @Query("SELECT * FROM camp_availability WHERE period_id = :periodId")
    List<CampAvailability> getAvailabilitiesForPeriod(int periodId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAvailability(CampAvailability availability);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAvailabilities(List<CampAvailability> availabilities);

    @Update
    void updateAvailability(CampAvailability availability);

    @Delete
    void deleteAvailability(CampAvailability availability);
}