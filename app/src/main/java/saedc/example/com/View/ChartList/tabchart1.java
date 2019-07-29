package saedc.example.com.View.ChartList;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import saedc.example.com.Model.Pojo.LinechartPojo;
import saedc.example.com.R;

/**
 * barch1
 * Created by saedc on 24/01/18.
 */

public class tabchart1 extends Fragment  {


    final String Tag = "chart";
    @BindView(R.id.barch1)
    LineChart lineChart;
    chartViewModel viewModel;
    String group_name;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM/yyyy");
    SimpleDateFormat Format = new SimpleDateFormat("dd");



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.tabchart1, container, false);

        ButterKnife.bind(this, rootview);

        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(chartViewModel.class);
        viewModel.linechartdata.observe(this::getLifecycle, new Observer<List<LinechartPojo>>() {
            @Override
            public void onChanged(@Nullable List<LinechartPojo> linechartPojos) {
                showTotalQuantityInUi(linechartPojos);
            }
        });


    }


    private void showTotalQuantityInUi(List<LinechartPojo> LinechartDataSource) {

        final List<Entry> entries = new ArrayList<>();


        Calendar now = Calendar.getInstance();


        for (int i = 0; i < LinechartDataSource.size(); i++) {

            if (dateFormat.format(now.getTime()).equals(dateFormat.format(LinechartDataSource.get(i).getSpendDate()))) {
                double Percentage = LinechartDataSource.get(i).getPrice();

                entries.add(new Entry(i, (float) Percentage));

            }

        }

        if (entries.isEmpty()) {
            entries.add(new Entry(0, (float) 0));
        }

//        XAxis xAxis = mChart.getXAxis();
//        xAxis.setEnabled(false);
//
//        YAxis yAxis = mChart.getAxisLeft();
//        yAxis.setEnabled(false);
//
//        YAxis yAxis2 = mChart.getAxisRight();
//        yAxis2.setEnabled(false);
        //changed
        lineChart.setNoDataText("لايوجد بيانات");
        lineChart.getLegend().setEnabled(false);
        lineChart.getXAxis().setSpaceMax(1f);
        lineChart.getXAxis().setSpaceMin(1f);

        lineChart.getXAxis().setEnabled(false);
        lineChart.getAxisLeft().setValueFormatter(new MyYAxisValueFormatter());
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getDescription().setEnabled(false);

        LineDataSet set1 = new LineDataSet(entries, "");
        //changed
        set1.setValueFormatter(new MyValueFormatter());
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        set1.setHighLightColor(Color.BLACK);
        //changed
        set1.setDrawValues(false);
        set1.setDrawCircles(true);
        set1.setColor(getResources().getColor(R.color.colorAccent));
//            set1.setCircleColor(R.color.colorAccent);
        set1.setLineWidth(2f);

        set1.setCircleRadius(5f);
        set1.setCircleColor(getResources().getColor(R.color.colorAccent));
        set1.setDrawCircleHole(false);
        set1.setDrawHighlightIndicators(false);
//            set1.setValueTextSize(15f);
        set1.setDrawFilled(false);
        set1.setFillDrawable(getResources().getDrawable(R.drawable.gradient));
//            set1.setFormLineWidth(1f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//            set1.setFormSize(15.f);

        LineData data = new LineData(set1);

        // set data
        if (set1 == null) {
            lineChart.clear();
            lineChart.invalidate();
        } else {
            lineChart.setData(data);
        }


    }


}
