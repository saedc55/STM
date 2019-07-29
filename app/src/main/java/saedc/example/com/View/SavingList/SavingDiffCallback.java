package saedc.example.com.View.SavingList;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import saedc.example.com.Model.Entity.Saving;

/**
 * Created by saedc on 10/02/18.
 */

public class SavingDiffCallback extends DiffUtil.Callback {
    private final List<Saving> oldSavingList;
    private final List<Saving> newSavingList;

    public SavingDiffCallback(List<Saving> oldEmployeeList, List<Saving> newEmployeeList) {
        this.oldSavingList = oldEmployeeList;
        this.newSavingList = newEmployeeList;
    }

    @Override
    public int getOldListSize() {
        return oldSavingList.size();
    }

    @Override
    public int getNewListSize() {
        return newSavingList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldSavingList.get(oldItemPosition).getId() ==
                newSavingList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

        return oldSavingList.get(oldItemPosition).equals(newSavingList.get(newItemPosition));
    }
}
