package saedc.example.com.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import saedc.example.com.R;
import saedc.example.com.View.AddAndEditSaving.AddAndEditSavingFragment;
import saedc.example.com.View.SavingList.SavingListFragment;

public class SavingActivity extends AppCompatActivity {

    FragmentManager fragmentManager;

    @BindView(R.id.fab1)
    FloatingActionButton fab;

    @BindView(R.id.toolbar1)
    Toolbar toolbar;

    @BindView(R.id.app_bar_layout1)
    AppBarLayout appBarLayout;
    @BindView(R.id.cloap)
    CollapsingToolbarLayout cloap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);
        ButterKnife.bind(this);


        final SharedPreferences shrd = getSharedPreferences("Data_User", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shrd.edit();
        String Name = shrd.getString("Name_User", "");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean("example_switch", false);

        String Disblyname = sharedPref.getString("example_text", "hello");

        setSupportActionBar(toolbar);

        cloap.setCollapsedTitleTextColor(Color.TRANSPARENT);
//        final Drawable upArrow = getResources().getDrawable(R.drawable.about);
//        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        if (switchPref) {
            getSupportActionBar().setTitle(Disblyname);
        }

//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fragmentManager = getSupportFragmentManager();
        fab.hide();
        if (getFragmentBackStackCount() == 0) {
            showRootFragment(SavingListFragment.newInstance());
            fab.show();
        }


    }

    public void showRootFragment(Fragment fragment) {
        appBarLayout.setExpanded(true);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container2, fragment)
                .commit();

    }

    @OnClick(R.id.fab1)
    public void openAddsavingFragment() {

        showFragment(AddAndEditSavingFragment.newInstance());


    }

    public void showFragment(Fragment nextFragment) {
        //be sure to not load same fragment
        if (isLastFragmentInBackstack(nextFragment)) {
            return;
        }

        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container2, nextFragment)
                .addToBackStack(nextFragment.getClass().getName())
                .commit();
        fab.hide();
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

    @Override
    public void onBackPressed() {

        int backStackCount = getFragmentBackStackCount();

        //if backstack == 1 it means this is last fragment so show fab
        switch (backStackCount) {
            case 1:
                fab.show();
                break;
        }


        super.onBackPressed();
    }
}
