package saedc.example.com.View.Summary.SummaryRecyclerView;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;


public class SummaryDiffCallback extends DiffUtil.Callback{

    private final List<String> oldsummaryList;
    private final List<String> newsummaryList;

    public SummaryDiffCallback(List<String> oldEmployeeList, List<String> newEmployeeList) {
        this.oldsummaryList = oldEmployeeList;
        this.newsummaryList = newEmployeeList;
    }

    @Override
    public int getOldListSize() {
        return oldsummaryList.size();
    }

    @Override
    public int getNewListSize() {
        return newsummaryList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldsummaryList.get(oldItemPosition) ==
                newsummaryList.get(newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final String oldsummary = oldsummaryList.get(oldItemPosition);
        final String newsummary = newsummaryList.get(newItemPosition);

        return oldsummary.equals(newsummary);
    }
}
