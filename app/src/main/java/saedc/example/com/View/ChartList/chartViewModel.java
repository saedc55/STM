package saedc.example.com.View.ChartList;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import saedc.example.com.Model.Pojo.LinechartPojo;
import saedc.example.com.Model.Pojo.PiechartPojo;
import saedc.example.com.Model.Pojo.Spending;
import saedc.example.com.Model.Repository.SpendingRepository;
import saedc.example.com.StmApp;

/**
 * Created by saedc on 09/02/18.
 */

public class chartViewModel extends AndroidViewModel {
    final public Double totalSpendingQuantity;
    final public List<Spending> chartDetail;
    final public List<Spending> spendingdate;
    final public List<Spending> spendingmonths;
    final public LiveData<List<PiechartPojo>> Pichartcategor;
    final public LiveData<List<LinechartPojo>> linechartdata;

    //    final public List<Double> totalSpendingforchart;
    @Inject
    public SpendingRepository spendingRepository;
    String group;
    Date dayst, dayet;


    public chartViewModel(Application application) throws ExecutionException, InterruptedException {
        super(application);
        ((StmApp) getApplication()).getAppComponent().inject(this);


        totalSpendingQuantity = GetTotalSpendingQuantity(dayst, dayet);


//        totalSpendingforchart = getTotalSpendingforchart();
        linechartdata = linechartData();
        spendingdate = getDate();
        Pichartcategor = Pichartcategory(dayst, dayet);
        spendingmonths = getMonths();
        chartDetail = ChartDetail(group);
//        liveDataMerger.addSource();
    }


    public Double GetTotalSpendingQuantity(Date dayst, Date dayet) throws ExecutionException, InterruptedException {
        return spendingRepository.getTotalSpendingQuantity1(dayst, dayet);
    }


    public List<Spending> getDate() throws ExecutionException, InterruptedException {
        return spendingRepository.GetSpendingsdate();
    }

    public LiveData<List<LinechartPojo>> linechartData() {
        return spendingRepository.LinechartData();
    }

    public List<Spending> ChartDetail(String groub) throws ExecutionException, InterruptedException {
        return spendingRepository.Chartdetail(groub);
    }

    public LiveData<List<PiechartPojo>> Pichartcategory(Date dayst, Date dayet) {
        return spendingRepository.pichartcategory(dayst, dayet);
    }


    public List<Spending> getMonths() throws ExecutionException, InterruptedException {
        return spendingRepository.GetSpendingsdatejustmonths();
    }


    public void deleteSpending(int id) {
        spendingRepository.deleteSpending(id);
    }

}
