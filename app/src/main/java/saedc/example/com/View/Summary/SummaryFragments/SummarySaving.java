package saedc.example.com.View.Summary.SummaryFragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import saedc.example.com.Model.Pojo.MaxSaving;
import saedc.example.com.R;
import saedc.example.com.View.Summary.SummaryRecyclerView.SummaryClickListener;
import saedc.example.com.View.Summary.SummaryRecyclerView.SummaryRecyclerViewAdapter;
import saedc.example.com.View.Summary.SummaryViewModel;


public class SummarySaving extends Fragment implements SummaryClickListener {


    SummaryViewModel viewModel;
    @BindView(R.id.savingchar)
    BarChart chart;
    Unbinder unbinder;
    SummaryRecyclerViewAdapter adapter;
    @BindView(R.id.summary_saving_Recycler)
    RecyclerView summarySavingRecycler;

    public SummarySaving() {


    }


    public static SummarySaving newInstance() {
        return new SummarySaving();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SummaryViewModel.class);
        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<String> Labels = new ArrayList<>();


        viewModel.savingChart().observe(this, maxSavings -> {
                    for (int i = 0; i < maxSavings.size(); i++) {
                        MaxSaving savingList = maxSavings.get(i);

                        float m = savingList.getMax().floatValue();
                        values1.add(new BarEntry(i, m));
                        Labels.add(savingList.getSavename());
                    }

                    SummarySaving.this.chartDataCustom(values1, Labels);
                    adapter.updateItems(Labels);
                }
        );

    }

    ArrayList<String> Label = new ArrayList<>();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary_saving, container, false);
        unbinder = ButterKnife.bind(this, view);
        adapter = new SummaryRecyclerViewAdapter(getActivity(), Label, this::onItemClick);
        summarySavingRecycler.setAdapter(adapter);
        summarySavingRecycler.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        return view;
    }

    private void chartDataCustom(ArrayList<BarEntry> values1, ArrayList<String> Labels) {


        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(Labels));


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);

        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        BarDataSet set1;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values1);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values1, String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));


        }
        set1.setColor(getResources().getColor(R.color.e1));
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);

        data.setBarWidth(0.9f);

        chart.setData(data);


    }

    @Override
    public void onItemClick(String pos) {
        Toast.makeText(getActivity(), pos, Toast.LENGTH_SHORT).show();
    }
}
