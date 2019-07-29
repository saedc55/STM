package saedc.example.com.Model.Pojo;

import androidx.room.ColumnInfo;

public class MaxSaving {
    @ColumnInfo(name = "name")
    String savename;
    @ColumnInfo(name = "item_price")
    Double price;
    @ColumnInfo(name = "max")
    Double max;

    public String getSavename() {
        return savename;
    }

    public void setSavename(String savename) {
        this.savename = savename;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "MaxSaving{" +
                "savename='" + savename + '\'' +
                ", price=" + price +
                ", max=" + max +
                '}';
    }
}
