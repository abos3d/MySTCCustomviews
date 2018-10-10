package sa.com.stc.customviews.pickers;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DateTimeSettings {

    //DatePickerDialog
    public DateItem selectedDate;
    public Calendar minDate;
    public Calendar maxDate;

    //TimePickerDialog
    public int selectedHour;
    public boolean is24HourMode;

    private List<DateItem> dateItemList;


    public DateTimeSettings() {
        minDate = Calendar.getInstance(Locale.ENGLISH);//Today is the minimum day
        is24HourMode = false;
    }

    public void setDateItemList(List<DateItem> dateItemList) {
        this.dateItemList = dateItemList;
    }

    public boolean isDateValid(int year, int month, int day) {
        if (dateItemList == null) return true;
        for (DateItem item : dateItemList)
            if (item.year == year && item.month == month && item.day == day)
                return true;
        return false;
    }

    public boolean isHourValid(int hour) {

        for (DateItem item : dateItemList)
            if (item.year == selectedDate.year && item.month == selectedDate.month && item.day == selectedDate.day)
                return item.availableTime.containsKey(hour);
        return false;
    }

    public boolean isMinuteValid(int minute) {

        for (DateItem item : dateItemList)
            if (item.year == selectedDate.year && item.month == selectedDate.month && item.day == selectedDate.day)
                if (item.availableTime.containsKey(selectedHour))
                    return item.availableTime.get(selectedHour).contains(minute);
        return false;

    }

    public void setSelectedDate(int year, int month, int day) {
        selectedDate = new DateItem(year, month, day);
    }

    public void setMaxDate(Calendar maxDate) {
        this.maxDate = maxDate;
    }

    public void setMinDate(Calendar minDate) {
        this.minDate = minDate;
    }

    public void setIs24HourMode(boolean is24HourMode) {
        this.is24HourMode = is24HourMode;
    }
}
