package saedc.example.com.Alarm;

import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import saedc.example.com.Model.Entity.RawSpending;
import saedc.example.com.NotificationHelper;
import saedc.example.com.View.MainActivity;
import saedc.example.com.View.SavingActivity;
import saedc.example.com.View.TotalSpendingPrice.TotalSpendingPriceViewModel;

// todo create new BroadcastReceiver for the salary
public class AlarmReceiver extends BroadcastReceiver {

    String TAG = "AlarmReceiver";
    TotalSpendingPriceViewModel viewModel;
    RawSpending Spending;
    //    AppDatabase db;
    SharedPreferences SettingsDataBase;
    SharedPreferences.Editor editor;
    NotificationHelper notificationHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        SettingsDataBase = PreferenceManager.getDefaultSharedPreferences(context);
        editor = SettingsDataBase.edit();

        notificationHelper = new NotificationHelper(context);
        Bundle saving = intent.getExtras();

        Calendar date = Calendar.getInstance();
//        db = Room.databaseBuilder(context, AppDatabase.class, "app_database").build();


        String name = saving.getString("savingName");
        Boolean enableNotification = SettingsDataBase.getBoolean("notifications_new_message", true);
        Toast.makeText(context, enableNotification.toString(), Toast.LENGTH_SHORT).show();
        if (enableNotification) {
            if (!(name == null)) {
//                remainingSalary();
                notificationHelper.setNotificationChannelId("1");
                notificationHelper.createNotification("لاتنسى ان تتدخر", "اضغط للذهاب للمدخرات");
                NotificationScheduler.showNotifications(context, SavingActivity.class,
                        "لاتنسى ان تتدخر", "اضغط للذهاب للمدخرات");
            }
            Log.d(TAG, "onReceive: ");
            if (name == null) {
                notificationHelper.setNotificationChannelId("2");
                notificationHelper.createNotification("مصاريفي", "إضافة مصاريفك تساعدك على معرفة اين يذهب مالك");
                NotificationScheduler.showNotification(context, MainActivity.class,
                        "مصاريفي", "إضافة مصاريفك تساعدك على معرفة اين يذهب مالك");

            }
        }


    }

//    public void remainingSalary() {
//
//        new Thread(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        double total = db.spendingDao().getTotalSpendingQuantit();
//                        int Total = (int) total;
//                        int salary = Integer.parseInt(SettingsDataBase.getString("Settings_salary", "0"));
//                        editor.putString("Settings_salary", String.valueOf(salary + (salary - Total)));
//                        editor.apply();
//
//                    }
//                }
//
//        ).start();
//    }
}


