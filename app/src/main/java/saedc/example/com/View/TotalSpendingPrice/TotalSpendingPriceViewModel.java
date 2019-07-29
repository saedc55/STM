package saedc.example.com.View.TotalSpendingPrice;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import saedc.example.com.Model.Pojo.IncomeAndSpendPojo;
import saedc.example.com.Model.Pojo.MaxSaving;
import saedc.example.com.Model.Pojo.PiechartPojo;
import saedc.example.com.Model.Repository.SpendingRepository;
import saedc.example.com.StmApp;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;


public class TotalSpendingPriceViewModel extends AndroidViewModel {


    final public LiveData<MaxSaving> maxsave;


    @Inject
    public SpendingRepository spendingRepository;


    public TotalSpendingPriceViewModel(Application application) {
        super(application);
        ((StmApp) getApplication()).getAppComponent().inject(this);

        maxsave = maxsaving();

    }


    public LiveData<IncomeAndSpendPojo> getTotalincome() {
        return spendingRepository.getTotalincome();
    }

    public LiveData<MaxSaving> maxsaving() {
        return spendingRepository.Maxsaving();
    }


    protected Double getSalary() throws ExecutionException, InterruptedException {

        return spendingRepository.getUserSalary();
    }


}
