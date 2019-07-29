package saedc.example.com.Model.Pojo;

import androidx.room.ColumnInfo;

/**
 * Created by saedc on 16/03/18.
 */

public class PiechartPojo {

    @ColumnInfo(name = "group_name")
    String categoryName;
    @ColumnInfo(name = "maxcount")
    Double categoryTotal;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Double getCategoryTotal() {
        return categoryTotal;
    }

    public void setCategoryTotal(Double categoryTotal) {
        this.categoryTotal = categoryTotal;
    }
}
