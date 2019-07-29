package saedc.example.com.View.TotalSpendingPrice;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import saedc.example.com.Model.Pojo.PiechartPojo;

/**
 * Created by Volkan Şahin on 2.09.2017.
 */

public class CategoryDiffCallback extends DiffUtil.Callback {

    private final List<PiechartPojo> oldSpendingList;
    private final List<PiechartPojo> newSpendingList;

    public CategoryDiffCallback(List<PiechartPojo> oldEmployeeList, List<PiechartPojo> newEmployeeList) {
        this.oldSpendingList = oldEmployeeList;
        this.newSpendingList = newEmployeeList;
    }

    @Override
    public int getOldListSize() {
        return oldSpendingList.size();
    }

    @Override
    public int getNewListSize() {
        return newSpendingList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldSpendingList.get(oldItemPosition) ==
                newSpendingList.get(newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldSpendingList.get(oldItemPosition).equals(newSpendingList.get(newItemPosition));

    }
}
