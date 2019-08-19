package saedc.example.com.Model.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import saedc.example.com.Model.Database.AppDatabase;
import saedc.example.com.Model.Entity.RawSpending;
import saedc.example.com.Model.Entity.Saving;
import saedc.example.com.Model.Entity.SpendingGroup;
import saedc.example.com.Model.Entity.User;
import saedc.example.com.Model.Pojo.AvgMaxMin;
import saedc.example.com.Model.Pojo.AvgTotal;
import saedc.example.com.Model.Pojo.AvgTotalSpendAndIncome;
import saedc.example.com.Model.Pojo.IncomeAndSpendPojo;
import saedc.example.com.Model.Pojo.LastMonth;
import saedc.example.com.Model.Pojo.LinechartPojo;
import saedc.example.com.Model.Pojo.MaxSaving;
import saedc.example.com.Model.Pojo.MonthInYear;
import saedc.example.com.Model.Pojo.MostAndLeast;
import saedc.example.com.Model.Pojo.PiechartPojo;
import saedc.example.com.Model.Pojo.Spending;


@Singleton
public class SpendingRepository {
    private AppDatabase appDatabase;

    @Inject
    public SpendingRepository(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }


    public LiveData<MaxSaving> Maxsaving() {
        return appDatabase.savingDao().maxsaving();
    }
    public LiveData<List<MaxSaving>> savingChart() {
        return appDatabase.savingDao().savingChart();
    }
    public LiveData<List<Saving>> Getsaving() {
        return appDatabase.savingDao().getsaving();
    }

    public LiveData<AvgMaxMin> getAvgMaxMinSending() {
        return appDatabase.spendingDao().getAvgMaxMinSending();
    }

    public LiveData<AvgTotalSpendAndIncome> getAvgMaxTotalSending(int month) {
        return appDatabase.spendingDao().getAvgMaxTotalSending(month);
    }
    public LiveData<MostAndLeast> getMostSending(int month) {
        return appDatabase.spendingDao().getMostSending(month);
    }
    public LiveData<MostAndLeast> getLeastSending(int month) {
        return appDatabase.spendingDao().getLeastSending(month);
    }


    public LiveData<AvgTotal> getAvgMaxTotalIncome(int month) {
        return appDatabase.spendingDao().getAvgMaxTotalIncome(month);
    }
    public LiveData<MostAndLeast> getMostIncome(int month) {
        return appDatabase.spendingDao().getMostIncome(month);
    }
    public LiveData<MostAndLeast> getLeastIncome(int month) {
        return appDatabase.spendingDao().getLeastIncome(month);
    }



    public LiveData<List<PiechartPojo>> getPichartByMonth(int month) {
        return appDatabase.spendingDao().getPichartByMonth(month);
    }

    public LiveData<AvgMaxMin> getAvgMaxMinIncome() {
        return appDatabase.spendingDao().getAvgMaxMinIncome();
    }

    public LiveData<List<MonthInYear>> getMonthInYear() {
        return appDatabase.spendingDao().MonthInYear(Calendar.getInstance().get(Calendar.YEAR));
    }
    public LiveData<List<LastMonth>> getLastMonthIncomeAndSpending() {
        return appDatabase.spendingDao().getLastMonthIncomeAndSpending();
    }
    public Double GetTotalSavingprice() throws ExecutionException, InterruptedException {

        Callable<Double> callable = () -> appDatabase.savingDao().getTotalSavingprice();

        Future<Double> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }







    public LiveData<User> getUserById(int id) {
        return appDatabase.UserDao().getUserById(id);
    }



    public Integer getUsersCounts() throws ExecutionException, InterruptedException {

        Callable<Integer> callable = () -> appDatabase.UserDao().getUsersCounts();

        Future<Integer> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();


    }





    public void deleteUser(int id) {
        appDatabase.UserDao().deleteUser(id);
    }

    public void AddUser(User u) {
        appDatabase.UserDao().addUser(u);
    }








    public void deletesaving(int id) {
        appDatabase.savingDao().deleteSaving(id);
    }

    public void Addsaving(Saving s) {
        appDatabase.savingDao().addSaving(s);
    }



    public LiveData<List<Spending>> getSpendings() {
        return appDatabase.spendingDao().getSpendingsWithGroups();
    }

    public LiveData<List<LinechartPojo>> LinechartData() {
        return appDatabase.spendingDao().Linechartdata();
    }

    public List<Spending> GetSpendingsdate() throws ExecutionException, InterruptedException {

        Callable<List<Spending>> callable = () -> appDatabase.spendingDao().getSpendingsdate();

        Future<List<Spending>> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }


    public List<Spending> Chartdetail(String Category) throws ExecutionException, InterruptedException {

        Callable<List<Spending>> callable = () -> appDatabase.spendingDao().chartdetail(Category);

        Future<List<Spending>> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }

    public LiveData<List<PiechartPojo>> pichartcategory(Date dayst, Date dayet) {
        return appDatabase.spendingDao().pichartcategory(dayst, dayet);
    }



    public LiveData<List<PiechartPojo>> CstegoryNameWithTotalList() {
        return appDatabase.spendingDao().CstegoryNameWithTotalList();
    }






    public List<Spending> GetSpendingsdatejustmonths() throws ExecutionException, InterruptedException {

        Callable<List<Spending>> callable = () -> appDatabase.spendingDao().getSpendingsdatejustmonths();

        Future<List<Spending>> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }




    public LiveData<Double> getTotalSpendingPrice1() {
        return appDatabase.spendingDao().getTotalSpendingPrice1();
    }

    public LiveData<Double> getTotalSpendingPrice2() {
        return appDatabase.spendingDao().getTotalSpendingPrice2();
    }









    public Double getLeftOverIncomeForLastMonth() throws ExecutionException, InterruptedException {

        Callable<Double> callable = () -> appDatabase.spendingDao().getLeftOverIncomeForLastMonth();

        Future<Double> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }

    public Double getUserSalary() throws ExecutionException, InterruptedException {

        Callable<Double> callable = () -> appDatabase.spendingDao().getUserSalary(1);

        Future<Double> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }












    public LiveData<IncomeAndSpendPojo> getTotalincome() {
        return appDatabase.spendingDao().getTotalincome();
    }


    public Double getTotalSpendingQuantity1(Date dayst, Date dayet) throws ExecutionException, InterruptedException {

        Callable<Double> callable = () -> appDatabase.spendingDao().getTotalSpendingQuantity1(dayst, dayet);

        Future<Double> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }




    public void addSpending(RawSpending s) {
        appDatabase.spendingDao().addSpending(s);
    }

    public LiveData<List<SpendingGroup>> getSpendingGroups() {
        return appDatabase.spendingGroupDao().getAllSpendingGroups();
    }

    public void deleteSpending(int id) {
        appDatabase.spendingDao().deleteSpending(id);
    }


}
