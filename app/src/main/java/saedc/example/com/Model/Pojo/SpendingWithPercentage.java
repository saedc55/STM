package saedc.example.com.Model.Pojo;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

import saedc.example.com.Model.Entity.RawSpending;

public class SpendingWithPercentage {

    @Embedded
    private RawSpending rawSpending;

    @ColumnInfo(name = "group_name")
    private String groupName;


    private Double percentage;

    public RawSpending getRawSpending() {
        return rawSpending;
    }

    public void setRawSpending(RawSpending spending) {
        this.rawSpending = spending;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public boolean isSame(SpendingWithPercentage s1) {
        return s1.getRawSpending().getDescription().equals(this.getRawSpending().getDescription())
                && s1.getRawSpending().getPrice().equals(this.getRawSpending().getPrice())
                && s1.getRawSpending().getGroupId() == this.getRawSpending().getGroupId()
                && s1.getGroupName().equals(this.getGroupName())
                && s1.getPercentage().equals(this.getPercentage());

    }
}
