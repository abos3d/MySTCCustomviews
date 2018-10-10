package sa.com.stc.customviews.pickers;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DateItem {

    public int day;
    public int month;
    public int year;
    public HashMap<Integer, List<Integer>> availableTime;// list of hours and its available minutes list

    public DateItem(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public DateItem(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
    }

    /**
     * HashMap<Hour,list of available minutes of the hour>
     *
     * @param availableTime
     */
    public void setAvailableTime(HashMap<Integer, List<Integer>> availableTime) {
        this.availableTime = availableTime;
    }

}

