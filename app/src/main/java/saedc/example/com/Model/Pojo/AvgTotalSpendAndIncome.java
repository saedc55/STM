package saedc.example.com.Model.Pojo;

import androidx.room.ColumnInfo;

public class AvgTotalSpendAndIncome {
    @ColumnInfo(name = "AVERAGE")
    Double average;
    @ColumnInfo(name = "INCOME")
    Double income;
    @ColumnInfo(name = "SPEND")
    Double spend;

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Double getSpend() {
        return spend;
    }

    public void setSpend(Double spend) {
        this.spend = spend;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }



}
