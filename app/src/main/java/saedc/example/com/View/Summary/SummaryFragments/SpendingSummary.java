package saedc.example.com.View.Summary.SummaryFragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import saedc.example.com.Model.Pojo.MonthInYear;
import saedc.example.com.R;
import saedc.example.com.View.Summary.DetailSummary;
import saedc.example.com.View.Summary.SummaryRecyclerView.SummaryClickListener;
import saedc.example.com.View.Summary.SummaryRecyclerView.SummaryRecyclerViewAdapter;
import saedc.example.com.View.Summary.SummaryViewModel;

import static saedc.example.com.View.Summary.MainSummaryActivity.MONTH;

public class SpendingSummary extends Fragment implements SummaryClickListener {


    private static final String ARG_SECTION_NUMBER = "section_number";
    @BindView(R.id.chartSummary)
    BarChart chart;
    Unbinder unbinder;

    Intent intent;
    Bundle bundle;
    SummaryViewModel viewModel;
    @BindView(R.id.recyclview_summary)
    RecyclerView recyclviewSummary;

    ArrayList<String> Labels = new ArrayList<>();

    public SpendingSummary() {
    }



    public static SpendingSummary newInstance() {
        SpendingSummary fragment = new SpendingSummary();

        return fragment;
    }

    SummaryRecyclerViewAdapter adapter;
    ArrayList<String> nonthList;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intent = new Intent(getActivity(), DetailSummary.class);
        viewModel = ViewModelProviders.of(this).get(SummaryViewModel.class);


        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();
        ArrayList<String> Label = new ArrayList<>();


        viewModel.getMonthInYear().observe(this, monthInYears -> {
                    for (MonthInYear s : monthInYears) {
                        float I = s.getIncome().floatValue();
                        float S = s.getSpend().floatValue();
                        values1.add(new BarEntry(s.getMonth(), S));
                        values2.add(new BarEntry(s.getMonth(), I));
                        Label.add(s.getMonthName());

                    }

                    chartDataCustom(values1, values2, Labels);
                    adapter.updateItems(Label);
                }

        );


    }


    private void chartDataCustom(ArrayList<BarEntry> spendingValues, ArrayList<BarEntry> Incomesvalues, ArrayList<String> Labels) {


        BarDataSet set1 = new BarDataSet(spendingValues, "مصاريف");
        BarDataSet set2 = new BarDataSet(Incomesvalues, "إيرادات");

        set1.setColor(getResources().getColor(R.color.e1));
        set1.setBarShadowColor(Color.rgb(203, 203, 203));

        set2.setColor(getResources().getColor(R.color.colorAccent));
        set2.setBarShadowColor(Color.rgb(203, 203, 203));


        float groupSpace = 0.08f;
        float barSpace = 0.06f; // x4 DataSet
        float barWidth = 0.4f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        int groupCount = 12;
        int startYear = 0;


        chart.getDescription().setEnabled(false);

//        chart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);

        chart.setDrawGridBackground(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it


        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(Labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(false);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        chart.getAxisRight().setEnabled(false);


        BarData data = new BarData(set1, set2);
//        data.setValueFormatter(new LargeValueFormatter());

        chart.setData(data);
        chart.getXAxis();

        // specify the width each bar should have
        chart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        chart.getXAxis().setAxisMinimum(startYear);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chart.getXAxis().setAxisMaximum(startYear + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chart.groupBars(startYear, groupSpace, barSpace);
        chart.invalidate();
    }












    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.spending_summary, container, false);
        unbinder = ButterKnife.bind(this, rootView);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        adapter = new SummaryRecyclerViewAdapter(getActivity(), Labels, this::onItemClick);
        recyclviewSummary.setAdapter(adapter);
        recyclviewSummary.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));


        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    void startActivity(int month) {
        bundle = new Bundle();
        bundle.putInt(MONTH, month);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onItemClick(String month) {
        switch (month) {
            case "يناير":
                startActivity(1);
                break;
            case "فبراير":
                startActivity(2);
                break;

            case "مارس":
                startActivity(3);
                break;

            case "ابريل":
                startActivity(4);
                break;

            case "مايو":
                startActivity(5);
                break;

            case "يونيو":
                startActivity(6);
                break;

            case "يوليو":
                startActivity(7);
                break;

            case "اغسطس":
                startActivity(8);
                break;

            case "سبتمبر":
                startActivity(9);
                break;

            case "اكتوبر":
                startActivity(10);
                break;

            case "نوفمبر":
                startActivity(11);
                break;

            case "ديسمبر":
                startActivity(12);
                break;

            default:
                break;


        }
    }
}

