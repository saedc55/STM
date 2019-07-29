package saedc.example.com.LoginPage;

import android.app.Application;
import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import saedc.example.com.Model.Entity.User;
import saedc.example.com.Model.Repository.SpendingRepository;
import saedc.example.com.StmApp;

public class LoginViewModel extends AndroidViewModel {
    @Inject
    public SpendingRepository spendingRepository;
    public LoginViewModel(@NonNull Application application) {
        super(application);
        ((StmApp) getApplication()).getAppComponent().inject(this);
    }

    public void AddNewUser(User s) {
        addUserTask task = new addUserTask();
        task.execute(s);
    }



    public void DeleteSaving(int id) {
        deleteUserTask task = new deleteUserTask();
        task.execute(id);
    }
    public Integer getUsersCounts() throws ExecutionException, InterruptedException {
        return spendingRepository.getUsersCounts();
    }


    class deleteUserTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            spendingRepository.deleteUser(integers[0]);

            return null;
        }
    }

    class addUserTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... savings) {
            spendingRepository.AddUser(savings[0]);
            return null;
        }
    }

}
