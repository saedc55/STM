package saedc.example.com.View.ChartList;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import saedc.example.com.CurrencyInstance;

public class MyYAxisValueFormatter implements IAxisValueFormatter {

    CurrencyInstance currencyInstance;

    public MyYAxisValueFormatter() {
        currencyInstance = new CurrencyInstance();

    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return currencyInstance.getFmt().format(value);
    }
}