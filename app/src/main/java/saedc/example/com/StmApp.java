package saedc.example.com;

import android.app.Application;

import saedc.example.com.Di.AppComponent;
import saedc.example.com.Di.AppModule;
import saedc.example.com.Di.DaggerAppComponent;


public class StmApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
