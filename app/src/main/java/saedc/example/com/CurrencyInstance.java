package saedc.example.com;

import android.os.Bundle;

import java.text.NumberFormat;
import java.util.Locale;

public class  CurrencyInstance {
    static NumberFormat numberFormat;
    Locale locale;
    private static CurrencyInstance instance;
    static {
        instance = new CurrencyInstance();
    }


     public static CurrencyInstance newInstance() {


         return instance;
    }
    private CurrencyInstance() {
        locale = new Locale("ar", "SA");
        numberFormat = NumberFormat.getCurrencyInstance(locale);
        numberFormat.setMinimumFractionDigits(2);

    }


    public static  NumberFormat getFmt() {
        return numberFormat;
    }


}
