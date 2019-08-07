package saedc.example.com.View.ChartList;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import saedc.example.com.Model.Pojo.Spending;
import saedc.example.com.R;


public class tabchart2 extends Fragment  {
    @BindView(R.id.listView1)
    ListView lv;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM/yyyy"); //you can add dd/MM/yyyy for date
    ArrayList<Spending> spendingList = new ArrayList<>();
    chartViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.tabchart2, container, false);
        ButterKnife.bind(this, rootview);

        return rootview;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(chartViewModel.class);

        List<Spending> Spendingdata1 = null;
        try {
            Spendingdata1 = viewModel.getMonths();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<BarData> list = new ArrayList<BarData>();

        for (int i = 1; i < Spendingdata1.size(); i++) {
            if (i == 1) {
                list.add(subscribeSpendings(Spendingdata1.get(0).getRawSpending().getDate()));
            } else {

                if (fetchOnlyMonthAndYear(Spendingdata1, i)) {
                    list.add(subscribeSpendings(Spendingdata1.get(i).getRawSpending().getDate()));
                }
            }


        }
//        viewModel.dateYearAndMonth().observe(this::getLifecycle, new Observer<List<String>>() {
//            @Override
//            public void onChanged(@Nullable List<String> spendings) {
//                if (spendings != null) {
//                    for (int i = 0; i <spendings.size() ; i++) {
//                        Log.d("dateYearAndMonth", spendings.get(i).toString());
//                    }
//                }
//
//            }
//        });

        ChartDataAdapter cda = new ChartDataAdapter(getContext(), list);
        lv.setAdapter(cda);

    }

    // compere dates to fetch only one month and year
    public Boolean fetchOnlyMonthAndYear(List<Spending> Spendingdata1, int Pos) {
        return !dateFormat.format(Spendingdata1.get(Pos).getRawSpending().getDate()).equals(dateFormat.format(Spendingdata1.get(Pos - 1).getRawSpending().getDate()));
    }

    private BarData subscribeSpendings(Date date) {

        List<Spending> Spendingdata = null;
        try {
            Spendingdata = viewModel.getDate();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Date> dates = new ArrayList<Date>();
        ArrayList<Double> quantity = new ArrayList<Double>();

        for (Spending group : Spendingdata) {

            dates.add(group.getRawSpending().getDate());
            quantity.add(group.getRawSpending().getPrice());

        }

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < dates.size(); i++) {

            if (dateFormat.format(date).equals(dateFormat.format(dates.get(i)))) {

                double pr = quantity.get(i);

                entries.add(new BarEntry(i, (float) (pr)));

            }

        }

        BarDataSet d = new BarDataSet(entries, dateFormat.format(date));

        d.setColors(ColorTemplate.MATERIAL_COLORS);
        d.setBarShadowColor(Color.rgb(203, 203, 203));

        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();
        sets.add(d);

        BarData barData = new BarData(sets);

        barData.setBarWidth(0.9f);
        return barData;


    }


    private class ChartDataAdapter extends ArrayAdapter<BarData> {

        public ChartDataAdapter(Context context, List<BarData> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            BarData data = getItem(position);

            ViewHolder holder = null;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.list_item_barchart, null);
                holder.chart = (BarChart) convertView.findViewById(R.id.chartSummary);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // apply styling

            data.setValueTextColor(Color.BLACK);
//            holder.chart.getLegend().setEnabled(false);

            holder.chart.getDescription().setEnabled(false);
            holder.chart.setDrawGridBackground(false);

            XAxis xAxis = holder.chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//
//            xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"ivukb","ouhij","ufiguh"}));
//
            xAxis.setDrawGridLines(false);
//            xAxis.setGranularity(1f);
//            xAxis.setGranularityEnabled(true);

            YAxis leftAxis = holder.chart.getAxisLeft();

            leftAxis.setLabelCount(5, false);
            leftAxis.setSpaceTop(15f);

            YAxis rightAxis = holder.chart.getAxisRight();

            rightAxis.setLabelCount(5, false);
            rightAxis.setSpaceTop(15f);

            // set data
            holder.chart.setData(data);
            holder.chart.setFitBars(true);

            // do not forget to refresh the chart
//            holder.chart.invalidate();
            holder.chart.animateY(700);

            return convertView;
        }

        private class ViewHolder {

            BarChart chart;
        }
    }


}
