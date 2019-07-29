package saedc.example.com.Model.Pojo;

import androidx.room.ColumnInfo;

public class MonthInYear {
    int month;
    Double income;
    Double spend;
    @ColumnInfo(name = "monthname")
    String monthName;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Double getIncome() {
        return income != null ? income : 0.0;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Double getSpend() {
        return spend != null ? spend : 0.0;
    }

    public void setSpend(Double spend) {
        this.spend = spend;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    @Override
    public String toString() {
        return "MonthInYear{" +
                "month=" + month +
                ", income=" + income +
                ", spend=" + spend +
                '}';
    }


}
