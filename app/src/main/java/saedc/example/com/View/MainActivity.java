package saedc.example.com.View;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

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
import saedc.example.com.View.Profile.Profile;
import saedc.example.com.View.SavingList.SavingListFragment;
import saedc.example.com.View.SpendingList.SpendingListFragment;
import saedc.example.com.View.SpendingList.SpendingListViewModel;
import saedc.example.com.View.Summary.MainSummaryActivity;
import saedc.example.com.View.TotalSpendingPrice.TotalSpendingPriceFragment;

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
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(R.style.settingTheme1);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SettingsDataBase = PreferenceManager.getDefaultSharedPreferences(this);


        if (SettingsDataBase.getBoolean("salaryRemainder", false)) {
            checkReminder();
        }


        viewModel = new ViewModelProvider(this).get(SpendingListViewModel.class);


        boolean IsColoreEnable = SettingsDataBase.getBoolean("color_preference", false);
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


        spendingButton.hide();
        savingButton.hide();
        //Add this fragment just at start and dont add to backstack
        if (getFragmentBackStackCount() == 0) {
            showRootFragment(SpendingListFragment.newInstance());
            spendingButton.show();
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    private void checkReminder() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

        final SharedPreferences shrd = this.getSharedPreferences("reminder", MODE_PRIVATE);
        final SharedPreferences.Editor editor = shrd.edit();

        if (!dateFormat.format(Calendar.getInstance().getTime()).equals(shrd.getString("reminder_date", "...."))) {


            new AlertDialog.Builder(this)
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

                                } catch (ExecutionException | InterruptedException e) {
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
        viewModel.getTotalSpendingQuantity().observe(this, aDouble -> {
            if (aDouble != null) {
                try {
                    ChangeThemeBaseOnThisMonthTotal(aDouble, viewModel.getUserSalary().intValue());
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void ChangeThemeBaseOnThisMonthTotal(double ThisMonthTotal, int salary) {
        double Percentag = ((ThisMonthTotal * 100.0) / salary);
        Percentag = Math.round(Percentag * 100.0) / 100.0;


        if (Percentag >= 100.0) {
            changeColor(R.color.d, R.color.e);
        } else if (Percentag >= 80.0) {
            changeColor(R.color.c, R.color.d);
        } else if (Percentag >= 60.0) {
            changeColor(R.color.a, R.color.c);
        } else if (Percentag >= 40.0) {
            changeColor(R.color.colorPrimary, R.color.a);
        } else {
            changeColor(R.color.colorPrimary, R.color.colorPrimary);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void changeColor(int colorStart, int colorEnd) {


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
            savingButton.setColorFilter((int) animator.getAnimatedValue());
            spendingButton.setColorFilter((int) animator.getAnimatedValue());
            TotalSpendingPriceFragment fragment = (TotalSpendingPriceFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_quantity);
            if (fragment != null) {
                fragment.changeTextColor((int) animator.getAnimatedValue());
            }

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
                Intent intent = new Intent(this, MainSummaryActivity.class);
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        nextFragmentName = nextFragment.getClass().getName();


        if (isLastFragmentInBackstack(nextFragment)) {
            return;
        }
        savingButton.hide();
        spendingButton.hide();
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
            return currentFragmentName.equals(nextFragmentName);
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
        final int id = item.getItemId();


        if (id == R.id.currncy) {

            Intent intent = new Intent(MainActivity.this, Taxcalculator.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.summray) {
            Intent intent = new Intent(MainActivity.this, MainSummaryActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.salaryProfile) {
            Intent intent = new Intent(MainActivity.this, Profile.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        }


        switch (item.getItemId()) {
            case R.id.spending:

                showRootFragment(SpendingListFragment.newInstance());
                spendingButton.setVisibility(VISIBLE);
                savingButton.setVisibility(View.GONE);


                return true;
            case R.id.saving:
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
        builder.setItems(R.array.dialog_choicelist, (dialog, which) -> {
            switch (which) {

                case 0:
                    showFragment(AddAndEditSpendingFragment.newInstance(true));
                    spendingButton.hide();
                    return;
                case 1:
                    showFragment(AddAndEditSpendingFragment.newInstance(false));
                    spendingButton.hide();
                    return;
            }
        });
        builder.show();

    }
}
