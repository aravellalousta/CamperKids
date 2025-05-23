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
}
