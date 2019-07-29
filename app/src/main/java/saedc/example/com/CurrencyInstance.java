package saedc.example.com;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyInstance {
    NumberFormat numberFormat;

    public CurrencyInstance() {
        Locale locale = new Locale("ar", "SA");
        numberFormat = NumberFormat.getCurrencyInstance(locale);
        numberFormat.setMinimumFractionDigits(2);

    }


    public NumberFormat getFmt() {
        return numberFormat;
    }


}
