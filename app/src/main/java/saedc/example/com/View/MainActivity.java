package saedc.example.com.View;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import saedc.example.com.About.AboutActivity;
import saedc.example.com.Model.Entity.RawSpending;
import saedc.example.com.Model.Repository.SpendingRepository;
import saedc.example.com.R;
import saedc.example.com.SettingsActivity;
import saedc.example.com.TaxCalculator.Taxcalculator;
import saedc.example.com.View.AddAndEditSaving.AddAndEditSavingFragment;
import saedc.example.com.View.AddAndEditSpending.AddAndEditSpendingFragment;
import saedc.example.com.View.ChartList.chartlist;
import saedc.example.com.View.SavingList.SavingListFragment;
import saedc.example.com.View.SpendingList.SpendingListFragment;
import saedc.example.com.View.SpendingList.SpendingListViewModel;
import saedc.example.com.View.Summary.MainSummaryActivity;

import static android.view.View.VISIBLE;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {


    @Inject
    public SpendingRepository spendingRepository;
    FragmentManager fragmentManager;
    InputMethodManager imm;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    Intent intent;
    Boolean mColorsInverted;
    @BindView(R.id.collaps)
    CollapsingToolbarLayout collaps;
    SpendingListViewModel viewModel;
    double Percentage;
    SharedPreferences SettingsDataBase;
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.saving_button)
    FloatingActionButton savingButton;
    @BindView(R.id.spending_button)
    FloatingActionButton spendingButton;
    String nextFragmentName = "";
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(R.style.settingTheme1);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SettingsDataBase = PreferenceManager.getDefaultSharedPreferences(this);


        checkReminder();

        viewModel = ViewModelProviders.of(this).get(SpendingListViewModel.class);

//        KeyboardVisibilityEvent.setEventListener(
//                this,
//                new KeyboardVisibilityEventListener() {
//                    @Override
//                    public void onVisibilityChanged(boolean isOpen) {
//                        // some code depending on keyboard visiblity status
//                        if (isOpen) { bottomNavigationView.setVisibility(View.GONE);
//                        } else bottomNavigationView.setVisibility(View.VISIBLE);
//
//                    }
//                });

        Boolean IsColoreEnable = SettingsDataBase.getBoolean("color_preference", false);
        if (IsColoreEnable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SubscribeThemeChanger();
            }
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        fragmentManager = getSupportFragmentManager();
        Boolean switchPref = SettingsDataBase.getBoolean("example_switch", false);
        String Disblyname = SettingsDataBase.getString("Settings_name", "hello");

        if (switchPref) {
//            getSupportActionBar().setTitle(Disblyname);
        }
        spendingButton.hide();
        savingButton.hide();
        //Add this fragment just at start and dont add to backstack
        if (getFragmentBackStackCount() == 0) {
            showRootFragment(SpendingListFragment.newInstance());
            spendingButton.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    private void checkReminder() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

        final SharedPreferences shrd = this.getSharedPreferences("reminder", MODE_PRIVATE);
        final SharedPreferences.Editor editor = shrd.edit();

        if (!dateFormat.format(Calendar.getInstance().getTime()).equals(shrd.getString("reminder_date", "...."))) {


            AlertDialog alertbox = new AlertDialog.Builder(this)
                    .setMessage("هل تريد اضافة الباقي من راتب الشهر الماضي؟")
                    .setPositiveButton("نعم", (arg0, arg1) -> {


                                try {
                                    Double totalSpending = viewModel.getLeftOverIncomeForLastMonth() != null ? viewModel.getLeftOverIncomeForLastMonth() : 0.0;

                                    Double salary = viewModel.getUserSalary() != null ? viewModel.getUserSalary() : 0.0;


                                    RawSpending s = new RawSpending();

                                    if (totalSpending > 0) {
                                        s.setSpend(false);
                                        s.setDate(Calendar.getInstance().getTime());
                                        s.setPrice(salary - totalSpending);
                                        s.setGroupId(7);
                                        s.setDescription("الفائض من الشهر الماضي");
                                        viewModel.addSpending(s);
                                    } else if (totalSpending < 0) {
                                        s.setSpend(true);
                                        s.setDate(Calendar.getInstance().getTime());
                                        s.setPrice(salary - totalSpending);
                                        s.setGroupId(7);
                                        s.setDescription("الناقص من الشهر الماضي");
                                        viewModel.addSpending(s);
                                    }

                                    editor.putString("reminder_date", dateFormat.format(Calendar.getInstance().getTime()));
                                    editor.apply();

                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                            }


                    )
                    .setNegativeButton("لا", (arg0, arg1) -> {

                        editor.putString("reminder_date", dateFormat.format(Calendar.getInstance().getTime()));
                        editor.apply();
                    })
                    .show();


        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void SubscribeThemeChanger() {
        viewModel.getTotalSpendingQuantity().observe(this::getLifecycle, aDouble -> {
            if (aDouble != null) {
                try {
                    ChangeThemeBaseOnThisMonthTotal(aDouble, viewModel.getUserSalary().intValue());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void ChangrColore(int colorStart, int colorEnd) {


        int colorFrom = getResources().getColor(colorStart);
        int colorTo = getResources().getColor(colorEnd);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(1000); // milliseconds
        colorAnimation.addUpdateListener(animator -> {

            //                1            CDDC39
            //                2            FFEB3B
            //                3            FFC107
            //                4            FF9800
            //                5            FF5722

            collaps.setBackgroundColor((int) animator.getAnimatedValue());
            collaps.setContentScrimColor((int) animator.getAnimatedValue());
            toolbar.setBackgroundColor((int) animator.getAnimatedValue());
            getWindow().setStatusBarColor((int) animator.getAnimatedValue());
//                totalSpendingPriceFragment.changeTextColor((int) animator.getAnimatedValue());

        });
        colorAnimation.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(getIntent());
                finish();
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_chart:
                intent = new Intent(this, chartlist.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showRootFragment(Fragment fragment) {

        appBarLayout.setExpanded(true);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container1, fragment)
                .commit();

    }

    @Override
    public void onBackPressed() {
        int backStackCount = getFragmentBackStackCount();
        //if backstack == 1 it means this is last fragment so show fab
//        switch (backStackCount) {
//            case 1:
////                spendingButton.show();
////                menuButton.showMenu(true);
//                break;
//        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (nextFragmentName.equals(AddAndEditSavingFragment.newInstance().getClass().getName())) {
            savingButton.show();
        } else if (nextFragmentName.equals(AddAndEditSpendingFragment.newInstance().getClass().getName()))
            spendingButton.show();


        super.onBackPressed();
    }

    public void showFragment(Fragment nextFragment) {
        //be sure to not load same
        nextFragmentName = nextFragment.getClass().getName();
//        Toast.makeText(this, nextFragment.getClass().getName(), Toast.LENGTH_SHORT).show();
        savingButton.hide();
        spendingButton.hide();

        if (isLastFragmentInBackstack(nextFragment)) {
            return;
        }

        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container1, nextFragment)
                .addToBackStack(nextFragment.getClass().getName())
                .commit();


//        menuButton.hideMenu(true);
        appBarLayout.setExpanded(false);
    }

    public boolean isLastFragmentInBackstack(Fragment fragment) {
        String currentFragmentName;
        String nextFragmentName = fragment.getClass().getName();

        //if count is 0 it means there isnt any fragment in backstack
        int count = getFragmentBackStackCount();
        if (count != 0) {
            currentFragmentName = getLastFragmentNameInBackStack();
            if (currentFragmentName.equals(nextFragmentName)) {
                return true;
            }
        }
        return false;
    }

    public String getLastFragmentNameInBackStack() {
        return fragmentManager.getBackStackEntryAt(getFragmentBackStackCount() - 1).getName();
    }

    public int getFragmentBackStackCount() {
        return fragmentManager.getBackStackEntryCount();
    }

    public void hideKeyboard(View v) {
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.currncy) {
            Intent intent = new Intent(this, Taxcalculator.class);
            startActivity(intent);
        } else if (id == R.id.about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.chartactivaty) {
            Intent intent = new Intent(this, chartlist.class);
            startActivity(intent);
        } else if (id == R.id.summray) {
            Intent intent = new Intent(this, MainSummaryActivity.class);
            startActivity(intent);
        }


        switch (item.getItemId()) {
            case R.id.spending:
//                SpendingListFragment spendingListFragment = new SpendingListFragment();
//                showFragment(spendingListFragment);
                showRootFragment(SpendingListFragment.newInstance());
                spendingButton.setVisibility(VISIBLE);
                savingButton.setVisibility(View.GONE);


                return true;
            case R.id.saving:
//                SavingListFragment savingListFragment = SavingListFragment.newInstance();
//                showFragment(savingListFragment);
                showRootFragment(SavingListFragment.newInstance());
                spendingButton.setVisibility(View.GONE);
                savingButton.setVisibility(VISIBLE);


                return true;

        }

        return true;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @OnClick(R.id.saving_button)
    public void onSavingButtonClicked() {
        showFragment(AddAndEditSavingFragment.newInstance());
        savingButton.hide();

    }

    @OnClick(R.id.spending_button)
    public void onSpendingButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.dialog_choicelist, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case 0:
                        showFragment(AddAndEditSpendingFragment.newInstance());
                        spendingButton.hide();
                        return;
                    case 1:
                        showFragment(AddAndEditSpendingFragment.newInstance(false));
                        spendingButton.hide();
                        return;
                }
            }
        });
        builder.show();

    }
}
