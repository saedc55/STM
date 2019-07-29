package saedc.example.com.TaxCalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import saedc.example.com.R;

public class Taxcalculator extends AppCompatActivity {
    EditText enterdmonay;
    EditText Tax;
    TextView Result;

    @BindView(R.id.toolbar6)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taxcalculator);
        setSupportActionBar(toolbar);

        enterdmonay = (EditText) findViewById(R.id.editText);
        Tax = (EditText) findViewById(R.id.Tax);
        Result = (TextView) findViewById(R.id.textView);
        enterdmonay.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() != 0 && !Tax.getText().toString().isEmpty()) {
                    double TaxAmount = Double.parseDouble(Tax.getText().toString());
                    double MoneyAmount = Double.parseDouble(s.toString());
                    double taxformel = (TaxAmount * MoneyAmount) / 100;
                    Result.setText(String.valueOf(taxformel));
                }

            }


        });

    }


}
