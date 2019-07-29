package saedc.example.com.View.AddAndEditSpending;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import android.os.AsyncTask;

import saedc.example.com.CurrentDate;
import saedc.example.com.Model.Entity.RawSpending;
import saedc.example.com.Model.Entity.SpendingGroup;
import saedc.example.com.Model.Repository.SpendingRepository;
import saedc.example.com.StmApp;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;


public class AddAndEditSpendingViewModel extends AndroidViewModel {

    final public LiveData<List<SpendingGroup>> spendingGroups;
    final public LiveData<Double> totalSpendingQuantity;
    @Inject
    public SpendingRepository spendingRepository;
    CurrentDate dateMonth = new CurrentDate();

    public AddAndEditSpendingViewModel(Application application) {
        super(application);
        ((StmApp) getApplication()).getAppComponent().inject(this);
        totalSpendingQuantity = getTotalSpendingQuantity();
        spendingGroups = getSpendingGroups();
    }

    public LiveData<List<SpendingGroup>> getSpendingGroups() {
        return spendingRepository.getSpendingGroups();
    }

    public LiveData<Double> getTotalSpendingQuantity() {
        return spendingRepository.getTotalSpendingPrice1();
    }

    protected Double getSalary() throws ExecutionException, InterruptedException {
        return spendingRepository.getUserSalary();
    }

    public void addSpending(RawSpending s) {

        addSpendingTask task = new addSpendingTask();
        task.execute(s);
    }

    public void deleteSpending(int id) {
        deleteSpendingTask task = new deleteSpendingTask();
        task.execute(id);
    }

    class deleteSpendingTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            spendingRepository.deleteSpending(integers[0]);
            return null;
        }
    }

    class addSpendingTask extends AsyncTask<RawSpending, Void, Void> {


        @Override
        protected Void doInBackground(RawSpending... rawSpendings) {
            spendingRepository.addSpending(rawSpendings[0]);
            return null;
        }
    }
}
