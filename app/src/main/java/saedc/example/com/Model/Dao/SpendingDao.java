package saedc.example.com.Model.Dao;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import saedc.example.com.Model.Entity.RawSpending;
import saedc.example.com.Model.Pojo.AvgMaxMin;
import saedc.example.com.Model.Pojo.AvgTotal;
import saedc.example.com.Model.Pojo.AvgTotalSpendAndIncome;
import saedc.example.com.Model.Pojo.IncomeAndSpendPojo;
import saedc.example.com.Model.Pojo.LastMonth;
import saedc.example.com.Model.Pojo.LinechartPojo;
import saedc.example.com.Model.Pojo.MonthInYear;
import saedc.example.com.Model.Pojo.MostAndLeast;
import saedc.example.com.Model.Pojo.PiechartPojo;
import saedc.example.com.Model.Pojo.Spending;


@Dao
public interface SpendingDao {


//------------------------ chart ------------------------
//    start

    @Query("SELECT date,quantity FROM SPENDING  ORDER BY SPENDING.DATE DESC")
    LiveData<List<LinechartPojo>> Linechartdata();

    @Query("SELECT SUM(QUANTITY)as maxcount,group_name FROM SPENDING,SPENDING_GROUP WHERE SPENDING.GROUP_ID = SPENDING_GROUP.GROUP_ID AND date BETWEEN :dayst AND :dayet  GROUP BY SPENDING_GROUP.GROUP_ID ORDER BY 'maxcount' DESC ")
    LiveData<List<PiechartPojo>> pichartcategory(Date dayst, Date dayet);

    @Query("SELECT * FROM SPENDING,SPENDING_GROUP WHERE SPENDING.GROUP_ID = SPENDING_GROUP.GROUP_ID and SPENDING_GROUP.group_name=:CategoryName ORDER BY SPENDING.DATE DESC")
    List<Spending> chartdetail(String CategoryName);

    @Query("SELECT * FROM SPENDING INNER JOIN SPENDING_GROUP ON SPENDING.GROUP_ID = SPENDING_GROUP.GROUP_ID ORDER BY SPENDING.DATE DESC")
    List<Spending> getSpendingsdate();

    @Query("SELECT * FROM SPENDING ORDER BY SPENDING.DATE ")
    List<Spending> getSpendingsdatejustmonths();

    @Query("SELECT SUM(QUANTITY) FROM SPENDING WHERE date BETWEEN :dayst AND :dayet")
    Double getTotalSpendingQuantity1(Date dayst, Date dayet);

//   end
//------------------------ chart ------------------------


//------------------------ SpendingListFragment ------------------------
//   start

    @Query("SELECT * FROM SPENDING,SPENDING_GROUP WHERE SPENDING.GROUP_ID = SPENDING_GROUP.GROUP_ID AND strftime('%Y-%m', datetime(SPENDING.DATE/1000, 'unixepoch')) = strftime('%Y-%m','now','localtime')  ORDER BY SPENDING.DATE DESC")
    LiveData<List<Spending>> getSpendingsWithGroups();

    @Query("SELECT SUM(QUANTITY)as maxcount,group_name FROM SPENDING,SPENDING_GROUP WHERE isSpend=1 AND SPENDING.GROUP_ID = SPENDING_GROUP.GROUP_ID AND strftime('%Y-%m', datetime(SPENDING.DATE/1000, 'unixepoch')) = strftime('%Y-%m','now','localtime') GROUP BY SPENDING_GROUP.GROUP_ID ORDER BY 'maxcount' DESC LIMIT 3")
    LiveData<List<PiechartPojo>> CstegoryNameWithTotalList();

//   end
//------------------------ SpendingListFragment ------------------------


    //------------------------ AddAndEditSpendingFragment ------------------------
//   start
    @Query("SELECT SUM(QUANTITY) FROM SPENDING WHERE isSpend=1 and strftime('%Y-%m', datetime(SPENDING.DATE/1000, 'unixepoch')) = strftime('%Y-%m','now','localtime')")
    LiveData<Double> getTotalSpendingPrice1();

    @Query("SELECT SUM(QUANTITY) FROM SPENDING WHERE strftime('%Y-%m', datetime(SPENDING.DATE/1000, 'unixepoch')) = strftime('%Y-%m','now','localtime')")
    LiveData<Double> getTotalSpendingPrice();

//   end
//------------------------ AddAndEditSpendingFragment ------------------------


//------------------------ MainActivity ------------------------
//   start


    @Query("SELECT SUM(QUANTITY) FROM SPENDING WHERE isSpend=1 and strftime('%Y-%m', datetime(SPENDING.DATE/1000, 'unixepoch')) = strftime('%Y-%m','now','localtime')")
    LiveData<Double> getTotalSpendingPrice2();



    @Query("SELECT SUM(QUANTITY) FROM SPENDING WHERE isSpend=1 and strftime('%Y-%m', datetime(SPENDING.DATE/1000, 'unixepoch')) = strftime('%Y-%m','now','localtime','start of month','-1 month')")
    Double getLeftOverIncomeForLastMonth();

    @Query("SELECT SALARY FROM  USER WHERE USER.id=:id")
    Double getUserSalary(int id);

//   end
//------------------------ MainActivity ------------------------


//------------------------ summary ------------------------
//   start

    //    int year = Calendar.getInstance().get(Calendar.YEAR);
    @Query("SELECT AVG(QUANTITY) AS average, MAX(QUANTITY) AS maximum , MIN(QUANTITY) AS minimum FROM SPENDING WHERE isSpend=1 AND datetime(SPENDING.date/1000, 'unixepoch') >= date('now','localtime', '-1 month') AND datetime(SPENDING.date/1000, 'unixepoch') < datetime('now','localtime', 'start of month')")
    LiveData<AvgMaxMin> getAvgMaxMinSending();

    @Query("SELECT AVG(QUANTITY) AS AVERAGE, SUM(case when isSpend = 0 then QUANTITY end ) AS INCOME,SUM(case when isSpend = 1 then QUANTITY end ) AS SPEND FROM SPENDING WHERE  CAST(strftime('%m', datetime(date/1000, 'unixepoch')) AS int)=:Month")
    LiveData<AvgTotalSpendAndIncome> getAvgMaxTotalSending(int Month);

    @Query("SELECT MAX(QUANTITY) AS price, group_name  FROM SPENDING,SPENDING_GROUP WHERE isSpend=1 AND SPENDING.GROUP_ID = SPENDING_GROUP.GROUP_ID AND CAST(strftime('%m', datetime(date/1000, 'unixepoch')) AS int)=:Month LIMIT 1 ")
    LiveData<MostAndLeast> getMostSending(int Month);

    @Query("SELECT MIN(QUANTITY) AS price, group_name  FROM SPENDING,SPENDING_GROUP WHERE isSpend=1 AND SPENDING.GROUP_ID = SPENDING_GROUP.GROUP_ID AND CAST(strftime('%m', datetime(date/1000, 'unixepoch')) AS int)=:Month LIMIT 1")
    LiveData<MostAndLeast> getLeastSending(int Month);




    @Query("SELECT AVG(QUANTITY) AS average, SUM(QUANTITY) AS total FROM SPENDING WHERE isSpend=0 AND CAST(strftime('%m', datetime(date/1000, 'unixepoch')) AS int)=:Month")
    LiveData<AvgTotal> getAvgMaxTotalIncome(int Month);

    @Query("SELECT MAX(QUANTITY) AS price, SPENDING.IncomeSource AS group_name  FROM SPENDING,SPENDING_GROUP WHERE isSpend=0 AND SPENDING.GROUP_ID = SPENDING_GROUP.GROUP_ID AND CAST(strftime('%m', datetime(date/1000, 'unixepoch')) AS int)=:Month LIMIT 1 ")
    LiveData<MostAndLeast> getMostIncome(int Month);

    @Query("SELECT MIN(QUANTITY) AS price,SPENDING.IncomeSource AS group_name  FROM SPENDING,SPENDING_GROUP WHERE isSpend=0 AND SPENDING.GROUP_ID = SPENDING_GROUP.GROUP_ID AND CAST(strftime('%m', datetime(date/1000, 'unixepoch')) AS int)=:Month LIMIT 1")
    LiveData<MostAndLeast> getLeastIncome(int Month);




    @Query("SELECT SUM(QUANTITY)as maxcount,group_name FROM SPENDING,SPENDING_GROUP WHERE isSpend=1 AND SPENDING.GROUP_ID = SPENDING_GROUP.GROUP_ID AND CAST(strftime('%m', datetime(date/1000, 'unixepoch')) AS int)=:Month GROUP BY SPENDING_GROUP.GROUP_ID ORDER BY 'maxcount' DESC ")
    LiveData<List<PiechartPojo>> getPichartByMonth(int Month);

    @Query("SELECT AVG(QUANTITY) AS average, MAX(QUANTITY) AS maximum , MIN(QUANTITY) AS minimum FROM SPENDING WHERE isSpend=1 AND datetime(SPENDING.date/1000, 'unixepoch') >= date('now','localtime', '-1 month') AND datetime(SPENDING.date/1000, 'unixepoch') < datetime('now','localtime', 'start of month')")
    LiveData<AvgMaxMin> getAvgMaxMinIncome();

    @Query("SELECT isSpend,  CAST(strftime('%d', datetime(date/1000, 'unixepoch')) AS int) as dayOfMonth,quantity as price FROM SPENDING WHERE  datetime(SPENDING.date/1000, 'unixepoch') >= date('now','localtime', '-1 month') AND datetime(SPENDING.date/1000, 'unixepoch') < datetime('now','localtime', 'start of month') ORDER BY SPENDING.DATE")
    LiveData<List<LastMonth>> getLastMonthIncomeAndSpending();

    @Query("SELECT CAST(strftime('%m', datetime(date/1000, 'unixepoch')) AS int) AS month,case strftime('%m', datetime(date/1000, 'unixepoch')) when '01' then 'يناير' when '02' then 'فبراير' when '03' then 'مارس' when '04' then 'ابريل' when '05' then 'مايو' when '06' then 'يونيو' when '07' then 'يوليو' when '08' then 'اغسطس' when '09' then 'سبتمبر' when '10' then 'اكتوبر' when '11' then 'نوفمبر' when '12' then 'ديسمبر' else '' end\n" +
            "as monthname  , SUM(case when isSpend = 0 then QUANTITY end ) AS income,SUM(case when isSpend = 1 then QUANTITY end ) AS spend FROM SPENDING WHERE CAST(strftime('%Y', datetime(date/1000, 'unixepoch')) AS int)=:Year  GROUP BY CAST(strftime('%m', datetime(date/1000, 'unixepoch')) AS int) ORDER BY DATE")
    LiveData<List<MonthInYear>> MonthInYear(int Year);


//   end
// ------------------------ summary ------------------------


//------------------------ TotalSpendingPriceFragment ------------------------
//   start


    @Query("SELECT SUM(case when isSpend = 0 then QUANTITY end ) AS INCOME,SUM(case when isSpend = 1 then QUANTITY end ) AS SPEND FROM SPENDING WHERE CAST(strftime('%Y', datetime(date/1000, 'unixepoch')) AS int) = CAST(strftime('%Y','now','localtime') AS int) AND CAST(strftime('%m', datetime(date/1000, 'unixepoch')) AS int) = CAST(strftime('%m','now','localtime') AS int)")
    LiveData<IncomeAndSpendPojo> getTotalincome();

//   end
//------------------------ TotalSpendingPriceFragment ------------------------


    @Query("DELETE FROM SPENDING WHERE ID = :id")
    void deleteSpending(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addSpending(RawSpending s);


}
