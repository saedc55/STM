package saedc.example.com.About;

/**
 * Created by woow on 3/17/18.
 */

import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

import saedc.example.com.R;

public class DevelopersActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttongp1, buttongp2, buttongp3, buttong1, buttong2, buttong3, buttona1, buttona2, buttona3;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);

        toolbar = (Toolbar) findViewById(R.id.toolbar_about1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //You can Delete any of the button by deleting layouts

            //Google Plus Link for First Card


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}