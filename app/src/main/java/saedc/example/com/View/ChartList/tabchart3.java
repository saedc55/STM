package saedc.example.com.View.ChartList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import saedc.example.com.CurrentDate;
import saedc.example.com.Model.Pojo.PiechartPojo;
import saedc.example.com.R;


public class tabchart3 extends Fragment implements  DatePickerDialog.OnDateSetListener {

    @BindView(R.id.barch2)
    PieChart pieChart;

    chartViewModel viewModel;
    String group_name;
    CurrentDate currentdate = new CurrentDate();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.tabchart3, container, false);
        ButterKnife.bind(this, rootview);

        return rootview;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(chartViewModel.class);
        subscribeTotalQuantity(currentdate.getStarT(), currentdate.getEnD());

    }


    private void subscribeTotalQuantity(Date star, Date end) {
//        viewModel.mediatorLiveData(star, end).observe(this::getLifecycle, new Observer<PieChartMerger>() {
//            @Override
//            public void onChanged(@Nullable PieChartMerger pieChartMerger) {
//                List<Spending> s=  pieChartMerger.getPiechartList();
////                Double a=pieChartMerger.getTotal();
//                for (Spending d:s) {
//                    Log.d("ssssssssss", "onChanged: "+d.getGroupName());
//                }
//            }
//
//        });


        viewModel.Pichartcategory(star, end).observe(this, new Observer<List<PiechartPojo>>() {
            @Override
            public void onChanged(@Nullable List<PiechartPojo> piechartPojos) {
                Double TotalPrice = null;
                try {
                    TotalPrice = viewModel.GetTotalSpendingQuantity(star, end);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                showTotalQuantityInUi(TotalPrice, piechartPojos);
                pieChart.invalidate();
            }
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
                Intent in = new Intent(getActivity(), DetilaChart.class);
                Bundle bundle = new Bundle();
                bundle.putString("Key1", pe.getLabel());
                in.putExtras(bundle);
                startActivity(in);
            }

            @Override
            public void onNothingSelected() {

            }
        });


    }


    @OnClick(R.id.datebutton)
    public void onViewClicked() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setEndTitle("النهاية");
        dpd.setStartTitle("البداية");
        dpd.show(getActivity().getFragmentManager(), "tabchart3");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

        Calendar enD = Calendar.getInstance();
        Calendar starT = Calendar.getInstance();


        starT.set(Calendar.YEAR, year);
        starT.set(Calendar.MONTH, monthOfYear);
        starT.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        enD.set(Calendar.YEAR, yearEnd);
        enD.set(Calendar.MONTH, monthOfYearEnd);
        enD.set(Calendar.DAY_OF_MONTH, dayOfMonthEnd);

        subscribeTotalQuantity(starT.getTime(), enD.getTime());

    }
}