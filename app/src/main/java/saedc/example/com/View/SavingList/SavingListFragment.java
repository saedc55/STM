package saedc.example.com.View.SavingList;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import saedc.example.com.Model.Entity.Saving;
import saedc.example.com.R;
import saedc.example.com.View.AddAndEditSaving.AddAndEditSavingFragment;
import saedc.example.com.View.MainActivity;

/**
 * Created by saedc on 11/02/18.
 */

public class SavingListFragment extends Fragment implements Recyclerviewclick {
    ArrayList<Saving> savingList = new ArrayList<>();
    SavingListViewModle viewModel;
    SavingRecyclerView adapter;
    View view;

    @BindView(R.id.saving_recyclerview)
    RecyclerView savingRecyclerView;
    @BindView(R.id.imageViewEmptyState)
    ImageView imageViewEmptyState;
    @BindView(R.id.TextViewEmptyState)
    TextView TextViewEmptyState;


    public static SavingListFragment newInstance() {

        return new SavingListFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_saving_list, container, false);
        ButterKnife.bind(this, view);

        adapter = new SavingRecyclerView(getActivity(), savingList, this);
        savingRecyclerView.setAdapter(adapter);
        savingRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

//        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
//        itemTouchhelper.attachToRecyclerView(savingRecyclerView);
//        savingRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//                swipeController.onDraw(c);
//            }
//        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SavingListViewModle.class);
        subscribeSavings();
    }

    private void subscribeSavings() {
        viewModel.Getsavings().observe(this, new Observer<List<Saving>>() {
            @Override
            public void onChanged(final List<Saving> savingss) {
                adapter.updateItems(savingss);
                onEmptytate(savingss);
            }
        });
    }

    @Override
    public void onItemClick(Saving clickedSpending) {


        ((MainActivity) getActivity()).showFragment(AddAndEditSavingFragment.newInstance(clickedSpending));

    }

    @Override
    public void onItemLongClick(int longClickedSpendingId) {
        AlertDialog alertbox = new AlertDialog.Builder(getActivity())
                .setMessage("هل انت متاكد من الحذف؟")
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {

                        viewModel.DeleteSaving(longClickedSpendingId);

                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }


    private void onEmptytate(List<Saving> dataset) {
        if (dataset.isEmpty()) {
            savingRecyclerView.setVisibility(View.GONE);
            imageViewEmptyState.setVisibility(View.VISIBLE);
            TextViewEmptyState.setVisibility(View.VISIBLE);
        } else {
            savingRecyclerView.setVisibility(View.VISIBLE);
            imageViewEmptyState.setVisibility(View.GONE);
            TextViewEmptyState.setVisibility(View.GONE);
        }

    }


}
