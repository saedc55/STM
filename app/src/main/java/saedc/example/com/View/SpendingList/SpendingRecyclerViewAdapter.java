package saedc.example.com.View.SpendingList;

import android.content.Context;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import saedc.example.com.CurrencyInstance;
import saedc.example.com.Model.Pojo.Spending;
import saedc.example.com.R;


public class SpendingRecyclerViewAdapter extends RecyclerView.Adapter<SpendingRecyclerViewAdapter.MyViewHolder> {
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy"); //you can add dd/MM/yyyy for date
    RecyclerVewItemClickListener recyclerVewItemClickListener;
    Context context;
    Double totalPrice;

    private List<Spending> spendings;
    private LayoutInflater layoutInflater;
    private int lastPosition = -1;

    SpendingRecyclerViewAdapter(Context context, ArrayList<Spending> spendings, RecyclerVewItemClickListener listener) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.spendings = spendings;
        this.recyclerVewItemClickListener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = layoutInflater.inflate(R.layout.item_spending_list_row, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        if (spendings != null && 0 <= position && position < spendings.size()) {
            Spending s = spendings.get(position);
            Double quantity = s.getRawSpending().getPrice();
            String description = s.getRawSpending().getDescription();
            Date date = s.getRawSpending().getDate();
            String group = s.getGroupName();
            String data = String.valueOf(s.getRawSpending().getId());

            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
            binderHelper.bind(viewHolder.swipeLayout, data);
            binderHelper.setOpenOnlyOne(true);
            // Bind your data here
            viewHolder.delete();
            viewHolder.edite();
//        String quantityWithCurrency = context.getString(R.string.turkish_lira_symbol) + String.valueOf(quantity);
            viewHolder.txtQuantity.setText(CurrencyInstance.newInstance().getFmt().format(quantity));
            viewHolder.txtDescription.setText(description);
            viewHolder.txtDate.setText(dateFormat.format(date));





            if (s.getRawSpending().getSpend()) {
                viewHolder.txtSpendingGroup.setText(group);
            } else viewHolder.txtSpendingGroup.setText(s.getRawSpending().getSource());


            setAnimation(viewHolder.itemView, position);
        }
    }

    public Double calculateDifference(Double spendingPrice, Double totalPrice) {

        Double result = null;

        result = ((spendingPrice * 100.0) / totalPrice);
        result = Math.round(result * 100.0) / 100.0;
        return result;
    }

    @Override
    public int getItemCount() {
        if (spendings == null)
            return 0;
        return spendings.size();
    }

    public void updateItems(List<Spending> spendings) {
        final SpendingDiffCallback diffCallback = new SpendingDiffCallback(this.spendings, spendings);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.spendings.clear();
        this.spendings.addAll(spendings);
        diffResult.dispatchUpdatesTo(this);
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
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

        @BindView(R.id.swipe_layout)
        SwipeRevealLayout swipeLayout;

        @BindView(R.id.front_layout)
        FrameLayout frontLayout;



//        @BindView(R.id.swipe)
//        SwipeLayout swipe;
//
//
//        @BindView(R.id.magnifier2)
//        ImageView edite;
//
//        @BindView(R.id.trash2)
//        ImageView trash;


        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }

        public void delete() {
            delete.setOnClickListener(view -> {

                Log.d("ListViewAdapter", "fillValues position: " + getAdapterPosition());
                recyclerVewItemClickListener.onItemLongClick(spendings.get(getAdapterPosition()).getRawSpending().getId());


            });
        }

        public void edite() {
            frontLayout.setOnClickListener(view -> {
                if (getAdapterPosition() != -1) {
                    recyclerVewItemClickListener.onItemClick(spendings.get(getAdapterPosition()).getRawSpending());
                }
            });

        }

    }
}
