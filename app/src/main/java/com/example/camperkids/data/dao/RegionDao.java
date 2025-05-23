package com.example.camperkids.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;

import java.util.List;

import com.example.camperkids.data.entities.Region;

@Dao
public interface RegionDao {
    @Query("SELECT * FROM regions")
    List<Region> getAllRegions();

    @Query("SELECT * FROM regions WHERE id = :id LIMIT 1")
    Region getRegionById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRegion(Region region);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRegions(List<Region> regions);

    @Update
    void updateRegion(Region region);

    @Delete
    void deleteRegion(Region region);
}
