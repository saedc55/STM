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
    TotalSpendingPriceViewModel viewModel;
    //    @BindView(R.id.recycl)
//    RecyclerView recyclerView;
    @BindView(R.id.salaryid)
    TextView SalaryTextView;
    @BindView(R.id.spindingsalary)
    TextView SpindingTextView;
    @BindView(R.id.totalincome)
    TextView totalIncome;
    Double salary = 0.0;
    // call SharedPreferences to take the salary from settings
    SharedPreferences SettingDatabase;
    CurrencyInstance numberFormat = new CurrencyInstance();
    Double total = 0.0;
    SharedPreferences SettingsDataBase;


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
        viewModel = ViewModelProviders.of(this).get(TotalSpendingPriceViewModel.class);
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


    public void calculationOfIncome() {

        //salary
        try {
            salary = viewModel.getSalary();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        viewModel.getTotalincome().observe(TotalSpendingPriceFragment.this::getLifecycle, aDouble -> {
            Double income;
            Double spend;
            spend = aDouble.getSpend() != null ? aDouble.getSpend() : 0.0;
            income = aDouble.getIncome() != null ? aDouble.getIncome() : 0.0;
            if (aDouble.getSpend() != null) {
//                    adapter.updateItems(aDouble.getSpend());
                SpindingTextView.setText(numberFormat.getFmt().format(aDouble.getSpend()));
                Boolean IsColoreEnable = SettingsDataBase.getBoolean("color_preference", false);
                if (IsColoreEnable) {
                    ChangeThemeBaseOnThisMonthTotal(aDouble.getSpend(), salary.intValue());
                }

            } else SpindingTextView.setText(R.string.empty);

            if (aDouble.getIncome() != null) {
                totalIncome.setText(numberFormat.getFmt().format(aDouble.getIncome()));

            } else totalIncome.setText(R.string.empty);


            SalaryTextView.setText(numberFormat.getFmt().format((salary + income) - spend));

        });

    }

    public void ChangeThemeBaseOnThisMonthTotal(double ThisMonthTotal, int salary) {
        double Percentag = ((ThisMonthTotal * 100.0) / salary);
        Percentag = Math.round(Percentag * 100.0) / 100.0;

        if (Percentag >= 20.0) {

            if (Percentag <= 40.0) {
                ChangrColore(R.color.colorPrimary, R.color.a);
            } else if (Percentag <= 60.0) {
                ChangrColore(R.color.a, R.color.c);
            } else if (Percentag <= 80.0) {
                ChangrColore(R.color.c, R.color.d);
            } else if (Percentag <= 100.0) {
                ChangrColore(R.color.d, R.color.e);
            }
        } else {
            ChangrColore(R.color.colorPrimary, R.color.colorPrimary);

        }


    }

    public void ChangrColore(int colorStart, int colorEnd) {


        int colorFrom = getResources().getColor(colorStart);
        int colorTo = getResources().getColor(colorEnd);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(1000); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {

                //                1            CDDC39
                //                2            FFEB3B
                //                3            FFC107
                //                4            FF9800
                //                5            FF5722

                SpindingTextView.setTextColor((int) animator.getAnimatedValue());
                SalaryTextView.setTextColor((int) animator.getAnimatedValue());
                totalIncome.setTextColor((int) animator.getAnimatedValue());

            }

        });
        colorAnimation.start();
    }


}
