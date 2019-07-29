package saedc.example.com.View.SavingList;

import saedc.example.com.Model.Entity.Saving;

/**
 * Created by saedc on 10/02/18.
 */

public interface Recyclerviewclick {
    void onItemClick(Saving clickedSpending);

    void onItemLongClick(int longClickedSpendingId);
}
