package saedc.example.com.View.SpendingList;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import saedc.example.com.Model.Entity.RawSpending;
import saedc.example.com.Model.Pojo.Spending;
import saedc.example.com.Model.Repository.SpendingRepository;
import saedc.example.com.StmApp;
import saedc.example.com.View.AddAndEditSpending.AddAndEditSpendingViewModel;


public class SpendingListViewModel extends AndroidViewModel {

    @Inject
    public SpendingRepository spendingRepository;

    public SpendingListViewModel(Application application) {
        super(application);
        ((StmApp) getApplication()).getAppComponent().inject(this);




    }






    public Double getLeftOverIncomeForLastMonth() throws ExecutionException, InterruptedException {
        return spendingRepository.getLeftOverIncomeForLastMonth();
    }

    public Double getUserSalary() throws ExecutionException, InterruptedException {
        return spendingRepository.getUserSalary();
    }


    public LiveData<Double> getTotalSpendingQuantity() {
        return spendingRepository.getTotalSpendingPrice2();
    }

    public LiveData<List<Spending>> getSpendings() {
        return spendingRepository.getSpendings();
    }



    public void deleteSpending(int id) {
        DeleteSaving task = new DeleteSaving(spendingRepository);
        task.execute(id);
    }


    public void addSpending(RawSpending s) {

        addSpendingTask task = new addSpendingTask();
        task.execute(s);
    }

    static class DeleteSaving extends AsyncTask<Integer, Void, Void> {
        SpendingRepository spendingRepository;

        public DeleteSaving(SpendingRepository spendingRepository) {
            this.spendingRepository = spendingRepository;
        }

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
