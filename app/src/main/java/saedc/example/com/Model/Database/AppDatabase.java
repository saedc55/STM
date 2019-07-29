package saedc.example.com.Model.Database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import saedc.example.com.Model.Dao.SavingDao;
import saedc.example.com.Model.Dao.SpendingDao;
import saedc.example.com.Model.Dao.SpendingGroupDao;
import saedc.example.com.Model.Dao.UserDao;
import saedc.example.com.Model.Entity.Account;
import saedc.example.com.Model.Entity.RawSpending;
import saedc.example.com.Model.Entity.Saving;
import saedc.example.com.Model.Entity.SpendingGroup;
import saedc.example.com.Model.Entity.User;


@Database(version = 4, entities = {RawSpending.class, SpendingGroup.class, Saving.class, User.class, Account.class})
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract SavingDao savingDao();

    public abstract SpendingDao spendingDao();

    public abstract SpendingGroupDao spendingGroupDao();

    public abstract UserDao UserDao();


}
