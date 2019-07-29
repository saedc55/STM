package saedc.example.com.View.SpendingList;

import saedc.example.com.Model.Entity.RawSpending;


public interface RecyclerVewItemClickListener {
    void onItemClick(RawSpending clickedSpending);

    void onItemLongClick(int longClickedSpendingId);
}
