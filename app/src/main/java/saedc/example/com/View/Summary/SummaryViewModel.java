package saedc.example.com.View.Summary;

import android.app.Application;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import saedc.example.com.Model.Pojo.AvgMaxMin;
import saedc.example.com.Model.Pojo.AvgTotal;
import saedc.example.com.Model.Pojo.LastMonth;
import saedc.example.com.Model.Pojo.MaxSaving;
import saedc.example.com.Model.Pojo.MonthInYear;
import saedc.example.com.Model.Pojo.MostAndLeast;
import saedc.example.com.Model.Pojo.PiechartPojo;
import saedc.example.com.Model.Repository.SpendingRepository;
import saedc.example.com.StmApp;

public class SummaryViewModel extends AndroidViewModel {

    @Inject
    public SpendingRepository spendingRepository;

    public SummaryViewModel(@NonNull Application application) {
        super(application);
        ((StmApp) getApplication()).getAppComponent().inject(this);
    }

    public LiveData<AvgMaxMin> getAvgMaxMinSending() {
        return spendingRepository.getAvgMaxMinSending();
    }

    public LiveData<List<MaxSaving>> savingChart() {
        return spendingRepository.savingChart();
    }

    public LiveData<MostAndLeast> getMostSending(int month) {
        return spendingRepository.getMostSending(month);
    }
    public LiveData<MostAndLeast> getLeastSending(int month) {
        return spendingRepository.getLeastSending(month);
    }

    public LiveData<AvgTotal> getAvgMaxTotalSending(int month) {
        return spendingRepository.getAvgMaxTotalSending(month);
    }



    public LiveData<MostAndLeast> getMostIncome(int month) {
        return spendingRepository.getMostIncome(month);
    }
    public LiveData<MostAndLeast> getLeastIncome(int month) {
        return spendingRepository.getLeastIncome(month);
    }

    public LiveData<AvgTotal> getAvgMaxTotalIncome(int month) {
        return spendingRepository.getAvgMaxTotalIncome(month);
    }




    public LiveData<List<PiechartPojo>> getPichartByMonth(int month) {
        return spendingRepository.getPichartByMonth(month);
    }




    public LiveData<AvgMaxMin> getAvgMaxMinIncome() {
        return spendingRepository.getAvgMaxMinIncome();
    }

    public LiveData<List<MonthInYear>> getMonthInYear() {
        return spendingRepository.getMonthInYear();
    }

    public LiveData<List<LastMonth>> getLastMonthIncomeAndSpending() {
        return spendingRepository.getLastMonthIncomeAndSpending();
    }
}
