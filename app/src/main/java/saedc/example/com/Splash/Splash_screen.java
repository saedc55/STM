package saedc.example.com.Splash;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import saedc.example.com.LoginPage.Login;
import saedc.example.com.R;

public class Splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        Thread  thread = new Thread(){
//
//            @Override
//            public void run() {
//                try {
//                    sleep(1500);
        startActivity(new Intent(this, Login.class));
        finish();
//                    finish();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        thread.start();

    }
}
