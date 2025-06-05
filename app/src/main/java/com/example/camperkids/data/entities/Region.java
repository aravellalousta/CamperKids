package com.example.camperkids.data.entities;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "regions")
public class Region {
    @PrimaryKey
    public int id;

    public String name;

    public String getName() {
        return name;
    }
}
