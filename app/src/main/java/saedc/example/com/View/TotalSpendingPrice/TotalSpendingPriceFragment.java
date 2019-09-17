package saedc.example.com.View.TotalSpendingPrice;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import saedc.example.com.CurrencyInstance;
import saedc.example.com.Model.Pojo.IncomeAndSpendPojo;
import saedc.example.com.Model.Pojo.PiechartPojo;
import saedc.example.com.R;

//this class for connecting database with header of home page
public class TotalSpendingPriceFragment extends Fragment {
    //    private static categoryRecyclerViewAdapter adapter;
    ArrayList<PiechartPojo> piechart = new ArrayList<>();
    private TotalSpendingPriceViewModel viewModel;
    //    @BindView(R.id.recycl)
//    RecyclerView recyclerView;
    @BindView(R.id.salaryid)
    TextView SalaryTextView;
    @BindView(R.id.spindingsalary)
    TextView SpindingTextView;
    @BindView(R.id.totalincome)
    TextView totalIncome;
    private Double salary = 0.0;
    // call SharedPreferences to take the salary from settings
    private SharedPreferences SettingDatabase;
    private CurrencyInstance numberFormat = new CurrencyInstance();
    Double total = 0.0;
    private SharedPreferences SettingsDataBase;


    public static TotalSpendingPriceFragment newInstance() {

        return new TotalSpendingPriceFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingDatabase = PreferenceManager.getDefaultSharedPreferences(getActivity());

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TotalSpendingPriceViewModel.class);
        SettingsDataBase = PreferenceManager.getDefaultSharedPreferences(getActivity());

        calculationOfIncome();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_total_spending_quantity, container, false);
        ButterKnife.bind(this, v);

        return v;
    }


    public void changeTextColor(int color){
         SalaryTextView.setTextColor(color);
         SpindingTextView.setTextColor(color);
         totalIncome.setTextColor(color);

    }


    private void calculationOfIncome() {

        //salary
        try {
            salary = viewModel.getSalary();
        } catch (NumberFormatException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        viewModel.getTotalincome().observe(TotalSpendingPriceFragment.this, aDouble -> {
            Double income;
            double spend;
            spend = aDouble.getSpend() != null ? aDouble.getSpend() : 0.0;
            income = aDouble.getIncome() != null ? aDouble.getIncome() : 0.0;
            if (aDouble.getSpend() != null) {
//                    adapter.updateItems(aDouble.getSpend());
                SpindingTextView.setText(numberFormat.getFmt().format(aDouble.getSpend()));
                boolean IsColoreEnable = SettingsDataBase.getBoolean("color_preference", false);


            } else SpindingTextView.setText(R.string.empty);

            if (aDouble.getIncome() != null) {
                totalIncome.setText(numberFormat.getFmt().format(aDouble.getIncome()));

            } else totalIncome.setText(R.string.empty);


            SalaryTextView.setText(numberFormat.getFmt().format((salary + income) - spend));

        });

    }





}
