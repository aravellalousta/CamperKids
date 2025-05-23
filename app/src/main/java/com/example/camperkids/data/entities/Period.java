package com.example.camperkids.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "periods")
public class Period {
    @PrimaryKey
    public Integer id;

    public String name;
    @ColumnInfo(name = "start_date")
    public String startDate;    // or use Date with a TypeConverter

    @ColumnInfo(name = "end_date")
    public String endDate;
}
