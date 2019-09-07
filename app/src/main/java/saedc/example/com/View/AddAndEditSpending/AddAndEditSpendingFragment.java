package saedc.example.com.View.AddAndEditSpending;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import saedc.example.com.Model.Entity.RawSpending;
import saedc.example.com.Model.Entity.SpendingGroup;
import saedc.example.com.R;
import saedc.example.com.View.MainActivity;


public class AddAndEditSpendingFragment extends Fragment implements View.OnFocusChangeListener {


    @BindView(R.id.save_button)
    ImageButton saveButton;

    @BindView(R.id.quantity_edittext)
    EditText quantityEditText;

    @BindView(R.id.description_edittext)
    EditText descriptionEditText;

    @BindView(R.id.title_textView)
    TextView titleTextView;


    @BindView(R.id.delete_button)
    ImageButton deleteButton;


    AddAndEditSpendingViewModel viewModel;
    RawSpending spending;
    View view;
    ArrayList<SpendingGroup> spendingGroupList;

    SharedPreferences SettingDatabase;


    @BindView(R.id.source)
    EditText source;

    @BindView(R.id.textInputLayout3)
    TextInputLayout textInputLayout3;
    private boolean check;
    @BindView(R.id.container)
    NestedScrollView container;
    private AlertDialog.Builder categoryDialog;
    @BindView(R.id.CategoryButton)
    Button CategoryButton;
    private int groupId;
    private boolean isEditMode;

    public static AddAndEditSpendingFragment newInstance() {
        return new AddAndEditSpendingFragment();
    }


    public static AddAndEditSpendingFragment newInstance(RawSpending spending) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("spending", spending);
        AddAndEditSpendingFragment fragment = new AddAndEditSpendingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static AddAndEditSpendingFragment newInstance(RawSpending spending, boolean isEditMode, boolean isSpend) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isSpend", isSpend);
        bundle.putParcelable("spending", spending);
        bundle.putBoolean("isEditMode", isEditMode);
        AddAndEditSpendingFragment fragment = new AddAndEditSpendingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static AddAndEditSpendingFragment newInstance(boolean isSpend) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isSpend", isSpend);
        AddAndEditSpendingFragment fragment = new AddAndEditSpendingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_and_edit_spending, container, false);
        ButterKnife.bind(this, view);
        quantityEditText.setOnFocusChangeListener(this);
        descriptionEditText.setOnFocusChangeListener(this);
        Bundle bundle = this.getArguments();


        boolean isSpend = bundle.getBoolean("isSpend");
        isEditMode = bundle.getBoolean("isEditMode");

        if (isSpend) {

            //create or edit spend
            if (isEditMode) {
                spending = bundle.getParcelable("spending");
                deleteButton.setVisibility(View.VISIBLE);
                titleTextView.setText("تعديل صرف");
            } else {
                spending = new RawSpending();
                spending.setSpend(true);
                titleTextView.setText("إنشاء صرف جديد");
            }


        } else {

            //create or edit income
            if (isEditMode) {
                spending = bundle.getParcelable("spending");
                inComeUi();
                titleTextView.setText("تعديل ايراد");
            } else {
                spending = new RawSpending();
                spending.setSpend(false);
                spending.setGroupId(1);
                inComeUi();
                titleTextView.setText("إنشاء ايراد جديد");

            }

        }


        return view;
    }

    public void inComeUi() {
        CategoryButton.setVisibility(View.GONE);
        textInputLayout3.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(AddAndEditSpendingViewModel.class);
        SettingDatabase = PreferenceManager.getDefaultSharedPreferences(getActivity());

            subscribeSpendingGroups();


    }

    private void subscribeSpendingGroups() {
        viewModel.spendingGroups.observe(this, spendingGroups -> {
            spendingGroupList = (ArrayList<SpendingGroup>) spendingGroups;
            if (spending.getId() != 0) {
                setTakenSpendingDataToFormElements(spending.getGroupId(), spendingGroupList);
            }
        });
    }

    public void setTakenSpendingDataToFormElements(int id, ArrayList<SpendingGroup> spendingGroupList) {

        ArrayList<String> stringGroup = new ArrayList<>();
        stringGroup.add("/////");
        for (SpendingGroup s : spendingGroupList) {
            stringGroup.add(s.getGroupName());
        }

        quantityEditText.setText(String.valueOf(spending.getPrice()));
        descriptionEditText.setText(spending.getDescription());
        source.setText(spending.getSource());
        CategoryButton.setBackgroundResource(R.drawable.button_bordered_save);
        CategoryButton.setTextColor(getResources().getColor(R.color.mdtp_white));
        CategoryButton.setText(stringGroup.get(id));
        groupId = id;
    }


    @OnClick(R.id.save_button)
    public void save() {

        String priceString = quantityEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String Source = source.getText().toString();
        Date date;
        if (spending.getDate() == null) {
            date = new Date(System.currentTimeMillis());
        } else {
            date = spending.getDate();
        }
        if (spending.getSpend()) {

            if (groupId == 0) {
                Toasty.warning(this.getContext(), "Please Select Group", Toast.LENGTH_SHORT, true).show();
                return;
            }

            if (priceString.equals("")) {
                Toasty.warning(this.getContext(), "Please write correct quantity", Toast.LENGTH_SHORT, true).show();
                return;
            }

            fillSpending(groupId, Double.valueOf(priceString), description, date, Source);

        } else {

            Toast.makeText(getActivity(), "income", Toast.LENGTH_SHORT).show();
            fillIncome(Double.valueOf(priceString), description, date, Source);

        }

//todo fix isRemainingSalaryEnough
//        if (!isRemainingSalaryEnough(Double.parseDouble(priceString))) {
//            return;
//        }


        addSpending(spending);


        hideKeyboard(container);
        // Success Toast
        Toasty.success(this.getContext(), "Success!", Toast.LENGTH_SHORT, true).show();
        getActivity().onBackPressed();
    }

    // check if the entered price less or equal to remaining salary
    // FIXME: 05/11/18 
    public boolean isRemainingSalaryEnough(double enterdPrice) {

        viewModel.totalSpendingQuantity.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(final Double price) {
                if (price != null) {

                    double remainingSalary = 0;
                    try {
                        remainingSalary = viewModel.getSalary() - price.intValue();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (enterdPrice > remainingSalary) {

                        check = false;
                        Toasty.warning(getActivity(), "السعر المدخل اكبر من باقي الراتب", Toast.LENGTH_SHORT, true).show();

                    } else {
                        check = true;
                    }


                }
            }
        });

        return check;


    }


    @OnClick(R.id.delete_button)
    public void deleteSpending() {
        deleteSpending(spending.id);
        getActivity().onBackPressed();
    }


    private void addSpending(RawSpending s) {
        viewModel.addSpending(s);
    }

    public void deleteSpending(int id) {
        viewModel.deleteSpending(id);
    }

    private void fillSpending(int groupId, double quantity, String description, Date date, String source) {
        spending.setGroupId(groupId);
        spending.setPrice(quantity);
        spending.setDescription(description);
        spending.setDate(date);
        spending.setSource(source);
    }

    private void fillIncome(double quantity, String description, Date date, String source) {
        spending.setPrice(quantity);
        spending.setDescription(description);
        spending.setDate(date);
        spending.setSource(source);
    }


    @Override
    public void onFocusChange(View view, boolean isFocused) {
        if (!isFocused) {
            hideKeyboard(view);
        }
    }


    private void hideKeyboard(View v) {
        ((MainActivity) getActivity()).hideKeyboard(v);
    }


    @OnClick(R.id.CategoryButton)
    void onViewClicked() {
        customDialog(spendingGroupList);

    }


    private void customDialog(List<SpendingGroup> spendingGroups) {
        categoryDialog = new AlertDialog.Builder(getActivity());


        ArrayList<String> stringGroup = new ArrayList<>();
        stringGroup.add("الرجاء إختيار النوع");
        for (SpendingGroup s : spendingGroups) {
            stringGroup.add(s.getGroupName());
        }
        categoryDialog.setNegativeButton("cancel", (dialog, which) -> {


        });

        categoryDialog.setItems(stringGroup.toArray(new String[0]), (dialog, which) -> {
            CategoryButton.setText(stringGroup.get(which));
            groupId = which;
            if (which != 0) {
                CategoryButton.setBackgroundResource(R.drawable.button_bordered_save);
                CategoryButton.setTextColor(getResources().getColor(R.color.mdtp_white));
            } else {
                CategoryButton.setBackgroundResource(R.drawable.button_bordered);
                CategoryButton.setTextColor(getResources().getColor(R.color.colorPrimary));
            }

        });


        categoryDialog.create().show();
    }
}
