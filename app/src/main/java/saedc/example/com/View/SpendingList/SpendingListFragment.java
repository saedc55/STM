package saedc.example.com.View.SpendingList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import saedc.example.com.CurrentDate;
import saedc.example.com.Model.Entity.RawSpending;
import saedc.example.com.Model.Pojo.Spending;
import saedc.example.com.R;
import saedc.example.com.View.AddAndEditSpending.AddAndEditSpendingFragment;
import saedc.example.com.View.MainActivity;

//this class connecting database withe Spending List in Home Page

public class SpendingListFragment extends Fragment implements RecyclerVewItemClickListener {
    ArrayList<Spending> spendingList = new ArrayList<>();
    SpendingListViewModel viewModel;
    SpendingRecyclerViewAdapter adapter;
    View view;

    @BindView(R.id.spending_recyclerview)
    RecyclerView spendingRecyclerView;
    @BindView(R.id.imageViewEmptyState)
    ImageView imageViewEmptyState;
    @BindView(R.id.TextViewEmptyState)
    TextView TextViewEmptyState;

    public static SpendingListFragment newInstance() {

        return new SpendingListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_spending_list, container, false);
        ButterKnife.bind(this, view);
        adapter = new SpendingRecyclerViewAdapter(getActivity(), spendingList, this);
        spendingRecyclerView.setAdapter(adapter);
        spendingRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SpendingListViewModel.class);


        subscribeSpendings();
    }

    private void subscribeSpendings() {



        viewModel.getTotalSpendingQuantity().observe(this, aDouble -> adapter.setTotalPrice(aDouble));

        viewModel.getSpendings().observe(this, spendings -> {
            adapter.updateItems(spendings);
            onEmptytate(spendings);
        });
    }

    private void onEmptytate(List<Spending> dataset) {
        if (dataset.isEmpty()) {
            spendingRecyclerView.setVisibility(View.GONE);
            imageViewEmptyState.setVisibility(View.VISIBLE);
            TextViewEmptyState.setVisibility(View.VISIBLE);
        } else {
            spendingRecyclerView.setVisibility(View.VISIBLE);
            imageViewEmptyState.setVisibility(View.GONE);
            TextViewEmptyState.setVisibility(View.GONE);
        }

    }

    @Override
    public void onItemClick(RawSpending clickedSpending) {
        ((MainActivity) getActivity()).showFragment(AddAndEditSpendingFragment.newInstance(clickedSpending));
    }

    // delete item whene user click on the item for long time
    @Override
    public void onItemLongClick(final int longClickedSpendingId) {
        AlertDialog alertbox = new AlertDialog.Builder(getActivity())
                .setMessage("هل انت متاكد من الحذف؟")
                .setPositiveButton("نعم", (arg0, arg1) -> viewModel.deleteSpending(longClickedSpendingId))
                .setNegativeButton("لا", (arg0, arg1) -> {
                })
                .show();

    }


}
