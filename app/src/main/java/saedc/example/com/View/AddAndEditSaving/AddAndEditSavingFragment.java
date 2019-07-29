package saedc.example.com.View.AddAndEditSaving;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.stephenvinouze.materialnumberpickercore.MaterialNumberPicker;
import com.tapadoo.alerter.Alerter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import saedc.example.com.Model.Entity.Saving;
import saedc.example.com.NotificationHelper;
import saedc.example.com.R;
import saedc.example.com.View.SavingActivity;


public class AddAndEditSavingFragment extends Fragment implements View.OnFocusChangeListener {

    @BindView(R.id.container1)
    NestedScrollView container;
    @BindView(R.id.save_button1)
    ImageButton saveButton;
    @BindView(R.id.name_saving)
    EditText name_savingEditText;
    @BindView(R.id.saving_price)
    EditText saving_priceEditText;
    @BindView(R.id.saving_price_amount)
    EditText saving_price_amountEditText;
    @BindView(R.id.title_textView1)
    TextView titleTextView;
    @BindView(R.id.textView2)
    TextView titleTextView2;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.delete_button1)
    ImageButton deleteButton;
    Double totalSaving;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.CalendarButton)
    ImageButton CalendarButton;

    SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
    Saving saving;
    View view;
    Calendar setcalendar;
    AddAndEditSavingViewModel viewModel;
    Date insert_date;
    Double lastprice = 0.0;
    String lastsavingname;
    Date lastdate;
    private double salary, op, save, nsave, maxsalary, NumberOfSaving;
    private int NumberOfMonth;
    private Timer timer;

    public static AddAndEditSavingFragment newInstance() {
        return new AddAndEditSavingFragment();
    }

    public static AddAndEditSavingFragment newInstance(Saving saving) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("saving", saving);
        AddAndEditSavingFragment fragment = new AddAndEditSavingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_and_edit_savings, container, false);
        ButterKnife.bind(this, view);
        name_savingEditText.setOnFocusChangeListener(this);
        saving_priceEditText.setOnFocusChangeListener(this);
        saving_price_amountEditText.setOnFocusChangeListener(this);

        Bundle bundle = this.getArguments();

        if (bundle == null || bundle.isEmpty()) {
            saving = new Saving();
        } else {
            saving = (Saving) bundle.getSerializable("saving");
            deleteButton.setVisibility(View.VISIBLE);
        }


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(AddAndEditSavingViewModel.class);
        OnEditeMode();

    }

    public void OnEditeMode() {
        if (saving.getId() != 0) {
            CalendarButton.setVisibility(View.GONE);
            name_savingEditText.setVisibility(View.GONE);
            saving_priceEditText.setVisibility(View.GONE);
            textView7.setVisibility(View.GONE);
            titleTextView2.setVisibility(View.GONE);
            amount.setVisibility(View.VISIBLE);
            seekbar.setVisibility(View.VISIBLE);

            setTakenSpendingDataToFormElements();
            seekbar.setMax(saving.getItem_price().intValue());
            seekbar.setProgress(saving.getItem_saveing().intValue());
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (progress >= saving.getItem_saveing().intValue()) {
                        progress = progress - saving.getItem_saveing().intValue();
                    } else {
                        progress = 0;
                    }
                    int incress = progress + saving.getItem_saveing().intValue();
                    amount.setText(String.valueOf("الزيادة " + progress));
                    saving_price_amountEditText.setText(String.valueOf(incress));


                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {


                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        }


        saving_priceEditText.addTextChangedListener(new TextWatcher() {
            boolean isCompleateType;

            @Override
            public void afterTextChanged(Editable s) {

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // do your actual work here
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());




                                if (!(s.equals("0") && s.length() == 0 && s.toString().isEmpty())) {

                                    try {
                                        salary = viewModel.getSalary();
                                        save = Double.valueOf(s.toString());
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }


                                }

                            }
                        });

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!s.toString().isEmpty()) {
                                    try {
                                        Suggester(salary);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }


                            }
                        });


                        // hide keyboard as well?
                        // InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        // in.hideSoftInputFromWindow(searchText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }, 600);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }

//                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
//                int switchPr = 0;
//
//                switchPr = Integer.parseInt(sharedPref.getString("Settings_salary", "0"));
//
//                if (!(s.equals("0") && s.length() == 0 && s.toString().isEmpty())) {
//
//                    try {
//                        save = Double.valueOf(s.toString());
//                    } catch (NumberFormatException e) {
//                        e.printStackTrace();
//                    }
//                    salary = switchPr;
//                    Suggester(salary);
//
//                }

            }


        });
    }


    public void Suggester(double Salary) throws ExecutionException, InterruptedException {

        if (!(Salary == 0)) {
            maxsalary = Salary * 40 / 100;

            if (Salary <= 700) {
                saveMoney(5);
            } else if (Salary <= 990) {
                saveMoney(10);
            } else if (Salary <= 3000) {
                saveMoney(15);
            } else if (Salary <= 5000) {
                saveMoney(20);
            } else if (Salary <= 7000) {
                saveMoney(25);
            } else if (Salary <= 10000) {
                saveMoney(30);
            } else if (Salary <= 11000) {
                saveMoney(35);
            } else {
                saveMoney(40);
            }

        } else {
            Toast.makeText(getActivity(), "الرجاء ادخال الراتب من اعدادت التطبيق", Toast.LENGTH_LONG).show();
        }

    }

    public void saveMoney(int percentage) throws ExecutionException, InterruptedException {


        op = salary * percentage / 100; // Part By %n Of The Salary.

        if (checkSavingLimit()) {
            nsave = save / op;
            NumberOfMonth = (int) Math.ceil(nsave);
            NumberOfSaving = save / NumberOfMonth;
//            textView7.setText(String.valueOf(NumberOfMonth) + " عدد الشهور المقترح ");

            if (NumberOfMonth / 12 > 0) {
                textView7.setText(String.valueOf(NumberOfMonth % 12 + " شهر " + NumberOfMonth / 12) + " سنة ");

            } else {
                textView7.setText(String.valueOf(NumberOfMonth) + " شهر ");

            }

            saving_price_amountEditText.setText(String.valueOf(Math.round(NumberOfSaving)));
            Alerter.create(getActivity())
                    .setTitle("مقترح")
                    .setText(String.format(" %.2f", NumberOfSaving) + " " + "المبلغ المقترح")
                    .enableProgress(false)
                    .setIcon(R.drawable.about)
                    .setBackgroundColorRes(R.color.colorAccent)
                    .show();
            setcalendar = Calendar.getInstance();
            setcalendar.set(Calendar.MONTH, NumberOfMonth);
            insert_date = setcalendar.getTime();
            titleTextView2.setText(formatter.format(insert_date));

        } else {

        }
    }

    public Boolean checkSavingLimit() throws ExecutionException, InterruptedException {

        if (viewModel.GetTotalSavingprice() != null) {
            totalSaving = viewModel.GetTotalSavingprice();
        } else {
            totalSaving = 0.0;
        }

//
        if (totalSaving <= maxsalary) {
            CompareBetweenFinalRessultAndTotalSaving(totalSaving, maxsalary);
            return true;
        } else {
            return false;
        }
    }

    // if the outcome lower than what in the database
    public double CompareBetweenFinalRessultAndTotalSaving(double totalSaving, double maxsalary) {
        double opertion;
        opertion = maxsalary - totalSaving;
        if (!(opertion == 0)) {
            if (opertion <= op) {
                op = opertion;
                Toast.makeText(getActivity(), "اقتربت من تجاوز الحد المسموح به", Toast.LENGTH_LONG).show();
            }
            return opertion;
        } else {
            return 0;
        }

    }


    public void setTakenSpendingDataToFormElements() {
        lastsavingname = saving.getSavingname();
        lastprice = saving.getItem_price();
        lastdate = saving.getEnd_date();
        titleTextView.setText(R.string.edit_a_saving);
        saving_price_amountEditText.setText(String.valueOf(saving.getItem_saveing()));
    }


    @OnClick(R.id.save_button1)
    public void save() {

        String savingname = name_savingEditText.getText().toString();
        String pricesave = saving_priceEditText.getText().toString();
        String priceamount = saving_price_amountEditText.getText().toString();
        Date date = new Date();

        if (!(saving.getId() != 0)) {
            if (!(titleTextView2.getText().length() == 0)) {

                date = insert_date;

            } else {
                Toasty.warning(this.getContext(), "الرجاء ادخال التاريخ", Toast.LENGTH_SHORT, true).show();
                return;
            }

            if (savingname.equals("")) {
                Toasty.warning(this.getContext(), "ادخل اسم المدخر", Toast.LENGTH_SHORT, true).show();
                return;
            }

            if (pricesave.equals("")) {
                Toasty.warning(this.getContext(), "الرجاء إدخال السعر", Toast.LENGTH_SHORT, true).show();
                return;
            }

            if (priceamount.equals("")) {
                Toasty.warning(this.getContext(), "الرجاء ادخال سعر الادخار", Toast.LENGTH_SHORT, true).show();
                return;
            }
        }


        if (saving.getId() != 0) {
            if (priceamount.equals("")) {
                Toasty.warning(this.getContext(), "الرجاء ادخال سعر الادخار", Toast.LENGTH_SHORT, true).show();
                return;
            }

            fillSaving(lastsavingname, lastprice, Double.valueOf(priceamount), lastdate);
        } else {
            fillSaving(savingname, Double.valueOf(pricesave), Double.valueOf(priceamount), date);
        }


        addSaving(saving);


        if (lastprice.intValue() != 0) {
            if (Double.valueOf(priceamount).intValue() >= lastprice.intValue()) {

                NotificationHelper notificationHelper = new NotificationHelper(getActivity());
                notificationHelper.createNotification("إنجاز %100", " اكتمل الهدف " + lastsavingname);

            }
        } else {

            if (Double.valueOf(priceamount).intValue() >= Double.valueOf(pricesave).intValue()) {

                Intent intent = new Intent(getActivity(), SavingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);


                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity(), "123")
                        .setSmallIcon(R.drawable.alarm_icon)
                        .setContentTitle("انجاز %100")
                        .setAutoCancel(true)
                        .setContentText(" اكتمل المدخر " + lastsavingname)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
                notificationManager.notify(1, mBuilder.build());
            }


        }


        Toasty.success(this.getContext(), "Success!", Toast.LENGTH_SHORT, true).show();

        getActivity().onBackPressed();
    }


    @OnClick(R.id.CalendarButton)
    public void selecttime() {


        final MaterialNumberPicker numberPicker = new MaterialNumberPicker(getActivity());
        numberPicker.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);
        numberPicker.setTextSize(100);
        new AlertDialog.Builder(getActivity())
                .setTitle("عدد الشهور")
                .setView(numberPicker)
                .setNegativeButton(getString(android.R.string.cancel), null)
                .setPositiveButton(getString(android.R.string.ok), (dialogInterface, i) -> {
                    setcalendar = Calendar.getInstance();
                    setcalendar.set(Calendar.MONTH, numberPicker.getValue());
                    insert_date = setcalendar.getTime();
                    titleTextView2.setText(formatter.format(insert_date));
                    if (numberPicker.getValue() / 12 > 0) {
                        textView7.setText(String.valueOf(numberPicker.getValue() % 12 + " شهر " + numberPicker.getValue() / 12) + " سنة ");

                    } else {
                        textView7.setText(String.valueOf(numberPicker.getValue()) + " شهر ");

                    }

                })
                .show();


    }

    @OnClick(R.id.delete_button1)
    public void deleteSpending() {
        deleteSpending(saving.id);
        getActivity().onBackPressed();
    }

    public void fillSaving(String name, double pricesav, double priceamou, Date date) {
        saving.setSavingname(name);
        saving.setItem_price(pricesav);
        saving.setItem_saveing(priceamou);
        saving.setEnd_date(date);
    }

    public void addSaving(Saving s) {
        viewModel.ADDSaving(s);
    }


    public void deleteSpending(int id) {
        viewModel.DeleteSaving(id);
    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }


}
