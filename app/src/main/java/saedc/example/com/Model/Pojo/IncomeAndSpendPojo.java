package saedc.example.com.Model.Pojo;

import androidx.room.ColumnInfo;

public class IncomeAndSpendPojo {
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
}
