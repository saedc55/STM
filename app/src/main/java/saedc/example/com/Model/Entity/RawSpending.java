package saedc.example.com.Model.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

import saedc.example.com.Model.Database.Converters;


@Entity(tableName = "spending")
public class RawSpending implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ForeignKey(entity = SpendingGroup.class, parentColumns = "group-id", childColumns = "group-id")
    @ColumnInfo(name = "group_id")
    private int groupId;

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "quantity")
    private Double Price;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "isSpend")
    private Boolean isSpend;

    @ColumnInfo(name = "IncomeSource")
    private String source;



    public int getId() {
        return id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double quantity) {
        this.Price = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getSpend() {
        return isSpend;
    }

    public void setSpend(Boolean spend) {
        isSpend = spend;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
