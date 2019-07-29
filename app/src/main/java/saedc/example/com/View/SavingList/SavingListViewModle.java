package saedc.example.com.View.SavingList;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import javax.inject.Inject;

import saedc.example.com.Model.Entity.Saving;
import saedc.example.com.Model.Repository.SpendingRepository;
import saedc.example.com.StmApp;

/**
 * Created by saedc on 11/02/18.
 */

public class SavingListViewModle extends AndroidViewModel {

    final public LiveData<List<Saving>> savings;

    @Inject
    public SpendingRepository spendingRepository;

    public SavingListViewModle(Application application) {
        super(application);
        ((StmApp) getApplication()).getAppComponent().inject(this);

        savings = Getsavings();

    }

    public LiveData<List<Saving>> Getsavings() {
        return spendingRepository.Getsaving();
    }

    public void DeleteSaving(int id) {
        DeleteSaving task = new DeleteSaving(spendingRepository);
        task.execute(id);
    }

    static class DeleteSaving extends AsyncTask<Integer, Void, Void> {
        SpendingRepository spendingRepository;

        public DeleteSaving(SpendingRepository spendingRepository) {
            this.spendingRepository = spendingRepository;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            spendingRepository.deletesaving(integers[0]);

            return null;
        }
    }
}
