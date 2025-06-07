package com.example.camperkids.data.entities;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(
        tableName = "camps",
        foreignKeys = @ForeignKey(
                entity = Region.class,
                parentColumns = "id",
                childColumns = "region_id"
        )
)
public class Camp {
    @PrimaryKey
    public Integer id;

    @NonNull
    public String name;

    @ColumnInfo(name = "region_id")
    public int regionId;

    @ColumnInfo(name = "village_name")
    public String villageName;

    public Double rating;

    public String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
