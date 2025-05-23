package com.example.camperkids.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(
        tableName = "camp_availability",
        foreignKeys = {
                @ForeignKey(entity = Camp.class, parentColumns = "id", childColumns = "camp_id"),
                @ForeignKey(entity = Period.class, parentColumns = "id", childColumns = "period_id")
        }
)
public class CampAvailability {
    @PrimaryKey
    public Integer id;

    @ColumnInfo(name = "camp_id")
    public int campId;

    @ColumnInfo(name = "period_id")
    public int periodId;

    @ColumnInfo(name = "available_spots")
    public Integer availableSpots;

    @ColumnInfo(name = "price_teenager")
    public Integer priceTeenager;

    @ColumnInfo(name = "price_child")
    public Integer priceChild;

    @ColumnInfo(name = "price_toddler")
    public Integer priceToddler;
}
