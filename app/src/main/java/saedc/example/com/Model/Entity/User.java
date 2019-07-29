package saedc.example.com.Model.Entity;

import java.util.Date;
import java.util.Objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import saedc.example.com.Model.Database.Converters;

@Entity(tableName = "user")
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String userName;

    @ColumnInfo(name = "salary")
    private Double salary;

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "salary_start_date")
    private Date salaryStartDate;


    public User() {
    }

    @Ignore
    public User(String userName, Double salary, Date salaryStartDate) {
        this.id = id;
        this.userName = userName;
        this.salary = salary;
        this.salaryStartDate = salaryStartDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Date getSalaryStartDate() {
        return salaryStartDate;
    }

    public void setSalaryStartDate(Date salaryStartDate) {
        this.salaryStartDate = salaryStartDate;
    }





    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", salary=" + salary +
                ", salaryStartDate=" + salaryStartDate +
                '}';
    }
}
