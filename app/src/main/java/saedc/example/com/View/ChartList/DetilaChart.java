package saedc.example.com.View.ChartList;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import saedc.example.com.Model.Entity.RawSpending;
import saedc.example.com.Model.Pojo.Spending;
import saedc.example.com.R;
import saedc.example.com.View.SpendingList.RecyclerVewItemClickListener;

public class DetilaChart extends AppCompatActivity implements RecyclerVewItemClickListener {

    chartViewModel viewModel;
    @BindView(R.id.rsycler)
    RecyclerView rsycler;
    ChartRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detila_chart);

        ButterKnife.bind(this);
        viewModel = new ViewModelProvider(this).get(chartViewModel.class);
        Bundle bundle = getIntent().getExtras();
        String valueReceived1 = bundle.getString("Key1");
//       String[] stringGroups = getResources().getStringArray(R.array.groups);

        try {
            adapter = new ChartRecyclerViewAdapter(this, (ArrayList<Spending>) viewModel.ChartDetail(valueReceived1), this);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        rsycler.setAdapter(adapter);
        rsycler.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));


    }



    @Override
    public void onItemClick(RawSpending clickedSpending) {


    }

    @Override
    public void onItemLongClick(int longClickedSpendingId) {
        viewModel.deleteSpending(longClickedSpendingId);
    }
}
