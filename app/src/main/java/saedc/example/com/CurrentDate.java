package saedc.example.com;

import java.util.Calendar;
import java.util.Date;

public class CurrentDate {
    Calendar enD = Calendar.getInstance();
    Calendar starT = Calendar.getInstance();

    public Date getEnD() {
        enD.set(Calendar.DATE, enD.getActualMaximum(Calendar.DAY_OF_MONTH));
        enD.set(Calendar.DATE, enD.getActualMaximum(Calendar.HOUR_OF_DAY));

        return enD.getTime();
    }

    public Date getStarT() {
        starT.set(Calendar.DATE, starT.getActualMinimum(Calendar.DAY_OF_MONTH));
        starT.set(Calendar.DATE, starT.getActualMinimum(Calendar.HOUR_OF_DAY));

        return starT.getTime();
    }
}
