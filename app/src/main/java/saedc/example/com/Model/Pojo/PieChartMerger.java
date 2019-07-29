package saedc.example.com.Model.Pojo;

import java.util.List;

public class PieChartMerger {
    List<Spending> piechartList;
    Double total;

    public List<Spending> getPiechartList() {
        return piechartList;
    }

    public void setPiechartList(List<Spending> piechartList) {
        this.piechartList = piechartList;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
