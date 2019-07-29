package saedc.example.com.View.Summary.SummaryRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import saedc.example.com.R;


public class SummaryRecyclerViewAdapter extends RecyclerView.Adapter<SummaryRecyclerViewAdapter.MyViewHolder> {
    Context context;
    private List<String> month;
    private LayoutInflater layoutInflater;
    private int lastPosition = -1;
    SummaryClickListener ClickListener;

    public SummaryRecyclerViewAdapter(Context context, ArrayList<String> month, SummaryClickListener ClickListener) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.month = month;
        this.ClickListener = ClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {
        View v = layoutInflater.inflate(R.layout.item_row_summary, viewGroup, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder viewHolder, int position) {
        if (month != null && 0 <= position && position < month.size()) {
            String s = month.get(position);
            viewHolder.monthButton.setText(s);

            viewHolder.monthButton.setOnClickListener(v -> ClickListener.onItemClick(s));


            setAnimation(viewHolder.itemView, position);
        }
    }


    @Override
    public int getItemCount() {
        if (month == null)
            return 0;
        return month.size();
    }

    public void updateItems(List<String> spending) {
        final SummaryDiffCallback diffCallback = new SummaryDiffCallback(this.month, spending);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.month.clear();
        this.month.addAll(spending);
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


        @BindView(R.id.month_button)
        Button monthButton;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }


    }
}
