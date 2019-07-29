package saedc.example.com.View.TotalSpendingPrice;

import android.content.Context;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import saedc.example.com.CurrencyInstance;
import saedc.example.com.Model.Pojo.PiechartPojo;
import saedc.example.com.R;
import saedc.example.com.View.SpendingList.RecyclerVewItemClickListener;


public class categoryRecyclerViewAdapter extends RecyclerView.Adapter<categoryRecyclerViewAdapter.MyViewHolder> {
    String[] stringGroups;
    TotalSpendingPriceViewModel viewModel;
    Context mContext;
    Double totalprice;
    RecyclerVewItemClickListener recyclerVewItemClickListener;
    Context context;
    Double total;
    private ArrayList<PiechartPojo> dataSet;
    private LayoutInflater layoutInflater;
    private int lastPosition = -1;

    public categoryRecyclerViewAdapter(Context context, ArrayList<PiechartPojo> dataSet, Double total) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.dataSet = dataSet;
        this.total = total;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = layoutInflater.inflate(R.layout.categorylistitem, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        PiechartPojo s = dataSet.get(position);
        Double price = s.getCategoryTotal();
        String group = s.getCategoryName();

        CurrencyInstance numberFormat = new CurrencyInstance();

        viewHolder.totaltext().setText(numberFormat.getFmt().format(price));

        double Percentage;
        if (total == null) {
            Percentage = ((price * 100.0) / price);
        } else {
            Percentage = ((price * 100.0) / total);
        }

        int rsult = (int) Percentage;
        viewHolder.progressBar().setProgress(rsult);

//        viewHolder.progressBar().setReachedBarColor(R.color.alerter_default_success_background);

        stringGroups = context.getResources().getStringArray(R.array.groups);

                viewHolder.groubnametext().setText(group);



        setAnimation(viewHolder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void updateItems(List<PiechartPojo> dataSet) {
        final CategoryDiffCallback diffCallback = new CategoryDiffCallback(this.dataSet, dataSet);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.dataSet.clear();
        this.dataSet.addAll(dataSet);
        diffResult.dispatchUpdatesTo(this);


    }

    public void updateItems(Double total) {
        this.total = total;
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation slide_up = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            viewToAnimate.startAnimation(slide_up);
            lastPosition = position;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.price1)
        TextView totaltext;

        @BindView(R.id.price2)
        TextView groubnametext;

        @BindView(R.id.progressBar3)
        NumberProgressBar progressBar;


        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public TextView totaltext() {
            return totaltext;
        }

        public TextView groubnametext() {
            return groubnametext;
        }

        public NumberProgressBar progressBar() {
            return progressBar;
        }


    }
}
