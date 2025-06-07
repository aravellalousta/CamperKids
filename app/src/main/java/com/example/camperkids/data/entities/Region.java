package com.example.camperkids.data.entities;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "regions")
public class Region implements Serializable {
    @PrimaryKey
    public int id;

    public String name;

    public String getName() {
        return name;
    }


}
