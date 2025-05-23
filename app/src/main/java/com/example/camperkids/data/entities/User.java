package com.example.camperkids.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "user")
public class User {
    @PrimaryKey
    public Integer id;

    public String name;

    public String email;

    @ColumnInfo(name = "phone_number")
    public String phoneNumber;
}
