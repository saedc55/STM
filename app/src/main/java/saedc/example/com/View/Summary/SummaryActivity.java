package saedc.example.com.View.Summary;

import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;
import saedc.example.com.CurrencyInstance;
import saedc.example.com.Model.Pojo.LastMonth;
import saedc.example.com.Model.Pojo.MonthInYear;
import saedc.example.com.R;
import saedc.example.com.View.ChartList.MyValueFormatter;
import saedc.example.com.View.ChartList.MyYAxisValueFormatter;

public class SummaryActivity extends AppCompatActivity {

    SummaryViewModel viewModel;
    @BindView(R.id.spend_max)
    TextView spendMax;
    @BindView(R.id.spend_min)
    TextView spendMin;
    @BindView(R.id.spend_avg)
    TextView spendAvg;
    @BindView(R.id.income_avg)
    TextView incomeAvg;
    @BindView(R.id.income_max)
    TextView incomeMax;
    @BindView(R.id.income_min)
    TextView incomeMin;
    @BindView(R.id.LineChart)
    LineChart LineChart;
    CurrencyInstance numberFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        ButterKnife.bind(this);
        numberFormat = CurrencyInstance.newInstance();
        viewModel = new ViewModelProvider(this).get(SummaryViewModel.class);

        viewModel.getAvgMaxMinSending().observe(this, avgMaxMin -> {
            if (avgMaxMin.getAverage() != null && avgMaxMin.getMaximum() != null && avgMaxMin.getMinimum() != null) {
                spendAvg.setText(numberFormat.getFmt().format(avgMaxMin.getAverage()));
                spendMax.setText(numberFormat.getFmt().format(avgMaxMin.getMaximum()));
                spendMin.setText(numberFormat.getFmt().format(avgMaxMin.getMinimum()));
            } else {
                spendAvg.setText("لايوجد");
                spendMax.setText("لايوجد");
                spendMin.setText("لايوجد");
            }

        });

        viewModel.getAvgMaxMinIncome().observe(this, avgMaxMin -> {
            if (avgMaxMin.getAverage() != null && avgMaxMin.getMaximum() != null && avgMaxMin.getMinimum() != null) {
                incomeAvg.setText(numberFormat.getFmt().format(avgMaxMin.getAverage()));
                incomeMax.setText(numberFormat.getFmt().format(avgMaxMin.getMaximum()));
                incomeMin.setText(numberFormat.getFmt().format(avgMaxMin.getMinimum()));
            } else {
                incomeAvg.setText("لايوجد");
                incomeMax.setText("لايوجد");
                incomeMin.setText("لايوجد");
            }

        });
        viewModel.getLastMonthIncomeAndSpending().observe(this::getLifecycle, lastMonth -> {
            for (LastMonth s : lastMonth) {
                showTotalQuantityInUi(lastMonth);

            }
        });


    }


    private void showTotalQuantityInUi(List<LastMonth> lastMonth) {

        final List<Entry> entries1 = new ArrayList<>();
        final List<Entry> entries2 = new ArrayList<>();


        for (LastMonth s : lastMonth) {

            double Percentage = s.getPrice();

            if (s.isSpend()) {

                entries1.add(new Entry(s.getDayOfMonth(), (float) Percentage));

            } else {

                entries2.add(new Entry(s.getDayOfMonth(), (float) Percentage));

            }

        }

        LineChart.setNoDataText("لايوجد بيانات");
        LineChart.getLegend().setEnabled(true);
        LineChart.getXAxis().setSpaceMax(1f);
        LineChart.getXAxis().setSpaceMin(1f);

        LineChart.getXAxis().setEnabled(false);
        LineChart.getAxisLeft().setValueFormatter(new MyYAxisValueFormatter());
//        LineChart.getAxisRight().setValueFormatter();
        LineChart.getAxisRight().setEnabled(false);
        LineChart.getAxisLeft().setDrawGridLines(false);
        LineChart.getXAxis().setDrawGridLines(false);
        Description description = new Description();
        description.setText("مقارنة المصاريف والايرادات");
        LineChart.setDescription(description);
//        LineChart.setVisibleXRangeMaximum(20); // allow 20 values to be displayed at once on the x-axis, not more
//        LineChart.moveViewToX(10);
        LineDataSet set1 = new LineDataSet(entries1, "spend");
        LineDataSet set2 = new LineDataSet(entries2, "income");

        set1.setValueFormatter(new MyValueFormatter());
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setDrawValues(false);
        set1.setDrawCircles(true);
        set1.setColor(getResources().getColor(R.color.e));
        set1.setLineWidth(2f);
        set1.setCircleRadius(5f);
        set1.setCircleColor(getResources().getColor(R.color.e));
        set1.setDrawCircleHole(false);
        set1.setDrawHighlightIndicators(false);
        set1.setDrawFilled(false);
        set1.setFillDrawable(getResources().getDrawable(R.drawable.gradient));

        set2.setValueFormatter(new MyValueFormatter());
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.setDrawValues(false);
        set2.setDrawCircles(true);
        set2.setColor(getResources().getColor(R.color.colorAccent));
        set2.setLineWidth(2f);
        set2.setCircleRadius(5f);
        set2.setCircleColor(getResources().getColor(R.color.colorAccent));
        set2.setDrawCircleHole(false);
        set2.setDrawHighlightIndicators(false);
        set2.setDrawFilled(false);
        set2.setFillDrawable(getResources().getDrawable(R.drawable.gradient));

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);

        LineData data = new LineData(dataSets);

        if (set1 == null) {
            LineChart.clear();
            LineChart.invalidate();
        } else {
            LineChart.setData(data);
        }


    }


}
