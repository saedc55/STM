package saedc.example.com.Model.Pojo;

import androidx.room.ColumnInfo;

public class AvgMaxMin {
    @ColumnInfo(name = "average")
    Double average;
    @ColumnInfo(name = "maximum")
    Double maximum;
    @ColumnInfo(name = "minimum")
    Double minimum;

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getMaximum() {
        return maximum;
    }

    public void setMaximum(Double maximum) {
        this.maximum = maximum;
    }

    public Double getMinimum() {
        return minimum;
    }

    public void setMinimum(Double minimum) {
        this.minimum = minimum;
    }
}
