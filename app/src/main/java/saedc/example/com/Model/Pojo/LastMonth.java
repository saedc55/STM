package saedc.example.com.Model.Pojo;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

public class LastMonth {
    @ColumnInfo(name = "price")
    Double price;
    @ColumnInfo(name = "dayOfMonth")
    int dayOfMonth;
    @ColumnInfo(name = "isSpend")
    boolean isSpend;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public boolean isSpend() {
        return isSpend;
    }

    public void setSpend(boolean spend) {
        isSpend = spend;
    }

    @Ignore
    @Override
    public String toString() {
        return "LastMonth{" +
                "price=" + price +
                ", dayOfMonth=" + dayOfMonth +
                ", isSpend=" + isSpend +
                '}';
    }
}
