package saedc.example.com.View.Profile;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import javax.inject.Inject;

import saedc.example.com.Model.Entity.User;
import saedc.example.com.Model.Repository.SpendingRepository;
import saedc.example.com.StmApp;

public class ProfileViewModel extends AndroidViewModel {

    @Inject
    public SpendingRepository spendingRepository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        ((StmApp) getApplication()).getAppComponent().inject(this);


    }

    LiveData<User> getUser() {

        return spendingRepository.getUserById(1);
    }

    protected void addUser(User user) {

        addUserTask task = new addUserTask();
        task.execute(user);
    }
    class addUserTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... savings) {
            spendingRepository.AddUser(savings[0]);
            return null;
        }
    }
}
