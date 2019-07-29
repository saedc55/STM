package saedc.example.com.Model.Pojo;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import java.util.Date;

import saedc.example.com.Model.Database.Converters;

public class LinechartPojo {

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "date")
    Date spendDate;

    @ColumnInfo(name = "quantity")
    Double price;

    public Date getSpendDate() {
        return spendDate;
    }

    public void setSpendDate(Date spendDate) {
        this.spendDate = spendDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
