package saedc.example.com.View.Summary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import saedc.example.com.Model.Pojo.PiechartPojo;
import saedc.example.com.R;
import saedc.example.com.View.ChartList.DetilaChart;

public class DetailSummary extends AppCompatActivity {

    SummaryViewModel viewModel;
    @BindView(R.id.avg)
    TextView avg;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.least)
    TextView least;
    @BindView(R.id.leastcategory)
    TextView leastcategory;
    @BindView(R.id.most)
    TextView most;
    @BindView(R.id.mostcategory)
    TextView mostcategory;
    @BindView(R.id.chartSummary)
    PieChart pieChart;


    int month;



    @BindView(R.id.avg_income)
    TextView avgIncome;
    @BindView(R.id.most_income)
    TextView mostIncome;
    @BindView(R.id.least_income)
    TextView leastIncome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(SummaryViewModel.class);

        month = getIntent().getIntExtra(MainSummaryActivity.MONTH, 1);

//        ValueAnimator animation = ValueAnimator.ofFloat(1000f, 0f);
//        animation.setDuration(700);
//        animation.start();
//
//        animation.addUpdateListener(updatedAnimation -> {
//            // You can use the animated value in a property that uses the
//            // same type as the animation. In this case, you can use the
//            // float value in the translationX property.
//            float animatedValue = (float)updatedAnimation.getAnimatedValue();
//            avg.setTranslationX(animatedValue);
//            total.setTranslationX(animatedValue);
//            least.setTranslationX(animatedValue);
//            leastcategory.setTranslationY(animatedValue);
//        });

//         avgIncome;
//
//        mostIncome;
//
//     leastIncome;

        viewModel.getAvgMaxTotalIncome(month).observe(this, avgTotal -> {
            avgIncome.setText(String.valueOf(avgTotal != null ? avgTotal.getAverage() : null));

        });


        viewModel.getLeastIncome(month).observe(this,mostAndLeast -> {

            leastIncome.setText(String.valueOf(mostAndLeast != null ? mostAndLeast.getPrice() : null));

        });


        viewModel.getMostIncome(month).observe(this,mostAndLeast -> {

            mostIncome.setText(String.valueOf(mostAndLeast != null ? mostAndLeast.getPrice() : null));


        });

        viewModel.getAvgMaxTotalSending(month).observe(this::getLifecycle, avgTotal -> {
            total.setText(String.valueOf(avgTotal != null ? avgTotal.getTotal() : null));
            avg.setText(String.valueOf(avgTotal != null ? avgTotal.getAverage() : null));

            viewModel.getPichartByMonth(month).observe(this::getLifecycle, piechartPojos ->
                    showTotalQuantityInUi(avgTotal != null ? avgTotal.getTotal() : null, piechartPojos));

        });


        viewModel.getMostSending(month).observe(this::getLifecycle, mostAndLeast -> {
            most.setText(String.valueOf(mostAndLeast != null ? mostAndLeast.getPrice() : null));
            mostcategory.setText(mostAndLeast != null ? mostAndLeast.getGroup_name() : null);
        });


        viewModel.getLeastSending(month).observe(this::getLifecycle, mostAndLeast -> {
            least.setText(String.valueOf(mostAndLeast != null ? mostAndLeast.getPrice() : null));
            leastcategory.setText(mostAndLeast != null ? mostAndLeast.getGroup_name() : null);
        });


    }

    private void showTotalQuantityInUi(Double quantity, List<PiechartPojo> piechart) {
        final List<PieEntry> entries = new ArrayList<>();

        if (quantity != null) {
            for (PiechartPojo Piechart : piechart) {
                if (Piechart.getCategoryTotal() != null) {
                    double Percentage = ((Piechart.getCategoryTotal() * 100.0) / quantity);


                    entries.add(new PieEntry((float) Percentage, Piechart.getCategoryName()));
                }
            }

        }

        chartCustomization(entries);

    }

    public void chartCustomization(List<PieEntry> entries) {

        PieDataSet set = new PieDataSet(entries, "");
        PieData data = new PieData(set);
        set.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set.setSelectionShift(15);
        data.setValueTextSize(14f);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        pieChart.animateY(1500);
//        pieChart.setCenterText("100%");
        pieChart.setCenterTextSize(45);
        pieChart.setCenterTextColor(R.color.centertext);
        pieChart.setEntryLabelColor(R.color.centertext);
        pieChart.setHoleRadius(50);
        pieChart.getDescription().setEnabled(false);
        pieChart.notifyDataSetChanged();
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                Intent in = new Intent(DetailSummary.this, DetilaChart.class);
                Bundle bundle = new Bundle();
                bundle.putString("Key1", pe.getLabel());
                in.putExtras(bundle);
                startActivity(in);
            }

            @Override
            public void onNothingSelected() {

            }
        });
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(true);

    }

}