package saedc.example.com.View.SavingList;

import android.content.Context;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import saedc.example.com.CurrencyInstance;
import saedc.example.com.Model.Entity.Saving;
import saedc.example.com.R;

/**
 * Created by saedc on 10/02/18.
 */
public class SavingRecyclerView extends RecyclerView.Adapter<SavingRecyclerView.MyViewHolder> {


    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    List<Saving> savingslist;
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d"); //you can add dd/MM/yyyy for date
    Recyclerviewclick recyclerVewItemClickListener;
    Context context;
    private LayoutInflater layoutInflater;
    private int lastPosition = -1;

    public SavingRecyclerView(Context context, ArrayList<Saving> spendings, Recyclerviewclick listener) {

        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.savingslist = spendings;
        this.recyclerVewItemClickListener = listener;
    }

    //    calculate days
    public static long getDayCount(String start, String end) {
        long diff = -1;
        try {
            Date dateStart = simpleDateFormat.parse(start);
            Date dateEnd = simpleDateFormat.parse(end);

            //time is always 00:00:00 so rounding should help to ignore the missing hour when going from winter to summer time as well as the extra hour in the other direction
            diff = Math.round((dateEnd.getTime() - dateStart.getTime()) / (double) 86400000);
        } catch (Exception e) {
            //handle the exception according to your own situation
        }
        return diff;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = layoutInflater.inflate(R.layout.item_saving_list_row, viewGroup, false);

        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Saving s = savingslist.get(position);
        String namesaving = s.getSavingname();
        Double quantity1 = s.getItem_price();
        Double quantity2 = s.getItem_saveing();
        CurrencyInstance numberFormat = new CurrencyInstance();
        Date date = s.getEnd_date();

        if (quantity2 <= quantity1) {
            double Percentage = ((quantity2 * 100.0) / quantity1);
            holder.getprogresbar().setProgress((int) Percentage);
        } else {
            holder.getprogresbar().setProgress(100);
        }
        holder.getTxtname().setText(namesaving);
        holder.getTxtPrice().setText(numberFormat.getFmt().format(quantity1));
        holder.getTxtprice_part().setText(numberFormat.getFmt().format(quantity2));
        int b = holder.getprogresbar().getProgress();
        holder.getTxtEndDate().setText(dateFormat.format(date));
        Date now = new Date(System.currentTimeMillis());


        if (holder.getprogresbar().getProgress() == 100) {
            holder.getimagebutton().setVisibility(View.VISIBLE);
        }
        holder.delete();
        holder.edite();
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return savingslist.size();
    }

    public void updateItems(List<Saving> savings) {
        final SavingDiffCallback diffCallback = new SavingDiffCallback(this.savingslist, savings);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.savingslist.clear();
        this.savingslist.addAll(savings);
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

        @BindView(R.id.item_price)
        TextView price;

        @BindView(R.id.item_name)
        TextView name;

        @BindView(R.id.item_price_part)
        TextView price_part;

        @BindView(R.id.progressBar2)
        NumberProgressBar bar;

        @BindView(R.id.endDate)
        TextView EndDate;



        @BindView(R.id.imageButton)
        ImageButton imageButton;
//        @BindView(R.id.switchico)
//        SwitchIconView switchico;

        @BindView(R.id.delete)
        TextView delete;

        @BindView(R.id.front_layout)
        FrameLayout frontLayout;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }

        public TextView getTxtPrice() {
            return price;
        }

        public ImageButton getimagebutton() {
            return imageButton;
        }

        public TextView getTxtname() {
            return name;
        }


        public NumberProgressBar getprogresbar() {
            return bar;
        }

        public TextView getTxtprice_part() {
            return price_part;
        }


        public TextView getTxtEndDate() {
            return EndDate;
        }



        public TextView getDelete() {
            return delete;
        }

        public void delete() {
            delete.setOnClickListener(view -> {
                if (getAdapterPosition() != -1) {
                    recyclerVewItemClickListener.onItemLongClick(savingslist.get(getAdapterPosition()).getId());
                }

            });
        }

        public void edite() {
            frontLayout.setOnClickListener(view -> {
                if (getAdapterPosition() != -1) {
                    recyclerVewItemClickListener.onItemClick(savingslist.get(getAdapterPosition()));
                }
            });


        }

    }


}

