package saedc.example.com.View.AddAndEditSaving;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import saedc.example.com.Model.Entity.Saving;
import saedc.example.com.Model.Repository.SpendingRepository;
import saedc.example.com.StmApp;

/**
 * Created by saedc on 12/02/18.
 */

public class AddAndEditSavingViewModel extends AndroidViewModel {
    final public Double totalsavings;
    @Inject
    public SpendingRepository spendingRepository;

    public AddAndEditSavingViewModel(Application application) throws ExecutionException, InterruptedException {
        super(application);
        ((StmApp) getApplication()).getAppComponent().inject(this);

        totalsavings = GetTotalSavingprice();
    }

    public void ADDSaving(Saving s) {
        addsavingTask task = new addsavingTask();
        task.execute(s);
    }

    public void DeleteSaving(int id) {
        deletesavingTask task = new deletesavingTask();
        task.execute(id);
    }

    protected Double getSalary() throws ExecutionException, InterruptedException {
        return spendingRepository.getUserSalary();
    }
    public Double GetTotalSavingprice() throws ExecutionException, InterruptedException {
        return spendingRepository.GetTotalSavingprice();
    }

    class deletesavingTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            spendingRepository.deletesaving(integers[0]);

            return null;
        }
    }

    class addsavingTask extends AsyncTask<Saving, Void, Void> {

        @Override
        protected Void doInBackground(Saving... savings) {
            spendingRepository.Addsaving(savings[0]);
            return null;
        }
    }
}
