package saedc.example.com.View.ChartList;

import android.content.Context;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import saedc.example.com.Model.Pojo.Spending;
import saedc.example.com.R;
import saedc.example.com.View.SpendingList.RecyclerVewItemClickListener;
import saedc.example.com.View.SpendingList.SpendingDiffCallback;


public class ChartRecyclerViewAdapter extends RecyclerView.Adapter<ChartRecyclerViewAdapter.MyViewHolder> {

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); //you can add dd/MM/yyyy for date
    RecyclerVewItemClickListener ClickListener;
    Context context;
    private List<Spending> spendings;
    private LayoutInflater layoutInflater;
    private int lastPosition = -1;

    public ChartRecyclerViewAdapter(Context context, ArrayList<Spending> spendings, RecyclerVewItemClickListener ClickListener) {
        this.ClickListener = ClickListener;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.spendings = spendings;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = layoutInflater.inflate(R.layout.item_spending_list_row, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        Spending s = spendings.get(position);
        Double quantity = s.getRawSpending().getPrice();
        String description = s.getRawSpending().getDescription();
        Date date = s.getRawSpending().getDate();
        String group = s.getGroupName();


        String quantityWithCurrency = context.getString(R.string.turkish_lira_symbol) + String.valueOf(quantity);
        viewHolder.txtQuantity.setText(quantityWithCurrency);
        viewHolder.txtDescription.setText(description);
        viewHolder.txtDate.setText(dateFormat.format(date));
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickListener.onItemLongClick(s.getRawSpending().getId());
            }
        });

                viewHolder.txtSpendingGroup.setText(group);




        setAnimation(viewHolder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return spendings.size();
    }

    public void updateItems(List<Spending> spendings) {
        final SpendingDiffCallback diffCallback = new SpendingDiffCallback(this.spendings, spendings);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.spendings.clear();
        this.spendings.addAll(spendings);
        diffResult.dispatchUpdatesTo(this);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation slide_up = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            viewToAnimate.startAnimation(slide_up);
            lastPosition = position;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtQuantity)
        TextView txtQuantity;

        @BindView(R.id.txtDescription)
        TextView txtDescription;

        @BindView(R.id.txtSpendingGroup)
        TextView txtSpendingGroup;

        @BindView(R.id.txtDate)
        TextView txtDate;

        @BindView(R.id.delete)
        TextView delete;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }


    }
}
