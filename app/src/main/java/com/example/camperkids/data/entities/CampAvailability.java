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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCampId() {
        return campId;
    }

    public void setCampId(int campId) {
        this.campId = campId;
    }

    public int getPeriodId() {
        return periodId;
    }

    public void setPeriodId(int periodId) {
        this.periodId = periodId;
    }

    public Integer getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(Integer availableSpots) {
        this.availableSpots = availableSpots;
    }

    public Integer getPriceTeenager() {
        return priceTeenager;
    }

    public void setPriceTeenager(Integer priceTeenager) {
        this.priceTeenager = priceTeenager;
    }

    public Integer getPriceChild() {
        return priceChild;
    }

    public void setPriceChild(Integer priceChild) {
        this.priceChild = priceChild;
    }

    public Integer getPriceToddler() {
        return priceToddler;
    }

    public void setPriceToddler(Integer priceToddler) {
        this.priceToddler = priceToddler;
    }
}
