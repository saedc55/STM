package saedc.example.com.View.ChartList;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.NumberFormat;
import java.util.Locale;

import saedc.example.com.CurrencyInstance;

public class MyValueFormatter implements IValueFormatter {

    CurrencyInstance currencyInstance;

    public MyValueFormatter() {
        currencyInstance = CurrencyInstance.newInstance();
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return currencyInstance.getFmt().format(value);
    }
}