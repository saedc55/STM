package saedc.example.com.LoginPage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.stephenvinouze.materialnumberpickercore.MaterialNumberPicker;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import es.dmoral.toasty.Toasty;
import saedc.example.com.Model.Entity.User;
import saedc.example.com.R;
import saedc.example.com.View.MainActivity;

public class Login extends AppCompatActivity {
    String date;
    Intent Intent;
    private ImageView btn_Next;
    private ImageButton calendarButto;
    private EditText uName;
    private EditText uNumber;
    private String Namee;
    private TextView info;
    LoginViewModel viewModel;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        btn_Next = findViewById(R.id.button2);
        calendarButto = findViewById(R.id.calendarButto);
        uName = findViewById(R.id.Name);
        uNumber = findViewById(R.id.Number);
        info = findViewById(R.id.info);



        final SharedPreferences shrd = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = shrd.edit();


        try {
            if (viewModel.getUsersCounts()>0) {
                Intent Intent = new Intent(this, MainActivity.class); //To Home Activity
                startActivity(Intent);
                finish();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //NumberPicker
        calendarButto.setOnClickListener(view -> {
            final MaterialNumberPicker numberPicker = new MaterialNumberPicker(Login.this);
            numberPicker.setTextColor(ContextCompat.getColor(Login.this, R.color.colorPrimary));
            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(30);
            numberPicker.setTextSize(100);
            new AlertDialog.Builder(Login.this)
                    .setTitle("يوم الراتب")
                    .setView(numberPicker)
                    .setNegativeButton(getString(android.R.string.cancel), null)
                    .setPositiveButton(getString(android.R.string.ok), (dialogInterface, i) -> date = String.valueOf(numberPicker.getValue()))
                    .show();

        });

        btn_Next.setOnClickListener(v -> {


            Namee = String.valueOf((uName.getText().toString()));
            if (Namee.toString().isEmpty() | uNumber.getText().toString().isEmpty() | date == null) { //check input values
                Toasty.info(Login.this, "هناك خانة فارغة", Toast.LENGTH_SHORT, true).show();

            } else {


                user = new User(uName.getText().toString(),Double.valueOf(uNumber.getText().toString()), Calendar.getInstance().getTime());

                Toast.makeText(this, user.toString(), Toast.LENGTH_LONG).show();
                viewModel.AddNewUser(user);
                Intent = new Intent(Login.this, MainActivity.class); //To Home Activity
                startActivity(Intent);
                finish();
            }


        });
    }
}
